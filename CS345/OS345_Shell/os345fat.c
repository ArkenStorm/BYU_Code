// os345fat.c - file management system	06/21/2020
// ***********************************************************************
// **   DISCLAMER ** DISCLAMER ** DISCLAMER ** DISCLAMER ** DISCLAMER   **
// **                                                                   **
// ** The code given here is the basis for the CS345 projects.          **
// ** It comes "as is" and "unwarranted."  As such, when you use part   **
// ** or all of the code, it becomes "yours" and you are responsible to **
// ** understand any algorithm or method presented.  Likewise, any      **
// ** errors or problems become your responsibility to fix.             **
// **                                                                   **
// ** NOTES:                                                            **
// ** -Comments beginning with "// ??" may require some implementation. **
// ** -Tab stops are set at every 3 spaces.                             **
// ** -The function API's in "OS345.h" should not be altered.           **
// **                                                                   **
// **   DISCLAMER ** DISCLAMER ** DISCLAMER ** DISCLAMER ** DISCLAMER   **
// ***********************************************************************
//
//		11/19/2011	moved getNextDirEntry to P6
//
// ***********************************************************************
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <setjmp.h>
#include <assert.h>
#include "os345.h"
#include "os345fat.h"

// ***********************************************************************
// ***********************************************************************
//	functions to implement in Project 6
//
int fmsCloseFile(int);
int fmsDefineFile(char*, int);
int fmsDeleteFile(char*);
int fmsOpenFile(char*, int);
int fmsReadFile(int, char*, int);
int fmsSeekFile(int, int);
int fmsWriteFile(int, char*, int);

// ***********************************************************************
// ***********************************************************************
//	Support functions available in os345p6.c
//
extern int fmsGetDirEntry(char* fileName, DirEntry* dirEntry);
extern int fmsGetNextDirEntry(int* dirNum, char* mask, DirEntry* dirEntry, int dir);

extern int fmsMount(char* fileName, void* ramDisk);

extern void setFatEntry(int FATindex, unsigned short FAT12ClusEntryVal, unsigned char* FAT);
extern unsigned short getFatEntry(int FATindex, unsigned char* FATtable);

extern int fmsMask(char* mask, char* name, char* ext);
extern void setDirTimeDate(DirEntry* dir);
extern int isValidFileName(char* fileName);
extern void printDirectoryEntry(DirEntry*);
extern void fmsError(int);

extern int fmsReadSector(void* buffer, int sectorNumber);
extern int fmsWriteSector(void* buffer, int sectorNumber);

int writeDirEntry(DirEntry* newEntry, char* mask, bool isNew);
int findCluster(int currentCluster);

// ***********************************************************************
// ***********************************************************************
// fms variables
//
// RAM disk
unsigned char RAMDisk[SECTORS_PER_DISK * BYTES_PER_SECTOR];

// File Allocation Tables (FAT1 & FAT2)
unsigned char FAT1[NUM_FAT_SECTORS * BYTES_PER_SECTOR];
unsigned char FAT2[NUM_FAT_SECTORS * BYTES_PER_SECTOR];

char dirPath[128];	 // current directory path
FCB OFTable[NFILES]; // open file table

extern bool diskMounted; // disk has been mounted
extern TCB tcb[];		 // task control block
extern int curTask;		 // current task #


// ***********************************************************************
// ***********************************************************************
// This function closes the open file specified by fileDescriptor.
// The fileDescriptor was returned by fmsOpenFile and is an index into the open file table.
//	Return 0 for success, otherwise, return the error number.
//
int fmsCloseFile(int fileDescriptor) {
	int error, currentCluster, nextCluster;
	// check for errors
	if (fileDescriptor < 0 || fileDescriptor >= NFILES)
		return ERR52;
	if (OFTable[fileDescriptor].name[0] == 0)
		return ERR63;

	DirEntry dirEntry;
	error = fmsGetDirEntry(OFTable[fileDescriptor].name, &dirEntry);
	if (error) { // possibly returns ERR61 (File Not Defined)
		return error;
	}
	if (OFTable[fileDescriptor].flags & FILE_ALTERED) {
		// update dirEntry
		if (dirEntry.startCluster == 0)
			dirEntry.startCluster = OFTable[fileDescriptor].startCluster;

		setDirTimeDate(&dirEntry);
		dirEntry.fileSize = OFTable[fileDescriptor].fileSize;
		error = writeDirEntry(&dirEntry, OFTable[fileDescriptor].name, FALSE);
		if (error)
			return error;
	}
	if (OFTable[fileDescriptor].flags & BUFFER_ALTERED) {
		// this flushes the buffer
		if ((error = fmsWriteSector(OFTable[fileDescriptor].buffer, C_2_S(OFTable[fileDescriptor].currentCluster))))
			return error;

		currentCluster = OFTable[fileDescriptor].currentCluster;
		nextCluster = getFatEntry(currentCluster, FAT1);
		setFatEntry(currentCluster, FAT_EOC, FAT1);
		currentCluster = nextCluster;

		while (currentCluster != FAT_EOC) {
			nextCluster = getFatEntry(currentCluster, FAT1);
			setFatEntry(currentCluster, 0, FAT1);
			currentCluster = nextCluster;
		}
	}
	OFTable[fileDescriptor].name[0] = 0;

	return 0;
} // end fmsCloseFile



// ***********************************************************************
// ***********************************************************************
// If attribute=DIRECTORY, this function creates a new directory
// file directoryName in the current directory.
// The directory entries "." and ".." are also defined.
// It is an error to try and create a directory that already exists.
//
// else, this function creates a new file fileName in the current directory.
// It is an error to try and create a file that already exists.
// The start cluster field should be initialized to cluster 0.  In FAT-12,
// files of size 0 should point to cluster 0 (otherwise chkdsk should report an error).
// Remember to change the start cluster field from 0 to a free cluster when writing to the
// file.
//
// Return 0 for success, otherwise, return the error number.
//
int fmsDefineFile(char* fileName, int attribute) {
	int error, startCluster;
	if (!isValidFileName(fileName))
		return ERR50;
	DirEntry dirEntry;
	if ((error = fmsGetDirEntry(fileName, &dirEntry)) == 0) // file is defined if getdirentry returns 0
		return ERR60;

	setDirTimeDate(&dirEntry);
	dirEntry.attributes = attribute;
	dirEntry.startCluster = 0;
	dirEntry.fileSize = 0;

	if (attribute == DIRECTORY) {
		DirEntry singleDot, doubleDot;
		startCluster = findCluster(0);
		if (startCluster < 0)
			return startCluster;
		int nameLen = strlen(fileName);
		for (int i = 0; i < nameLen; i++) {
			dirEntry.name[i] = toupper(fileName[i]);
		}
		for (int i = nameLen; i < 8; i++) {
			dirEntry.name[i] = ' ';
		}
		for (int i = 0; i < 3; i++) {
			dirEntry.extension[i] = singleDot.extension[i] = doubleDot.extension[i] = ' ';
		}
		dirEntry.startCluster = startCluster;

		singleDot.startCluster = startCluster;
		doubleDot.startCluster = CDIR;
		singleDot.name[0] = doubleDot.name[0] = doubleDot.name[1] = '.';
		singleDot.name[1] = ' ';
		singleDot.attributes = doubleDot.attributes = DIRECTORY;
		singleDot.time = doubleDot.time = dirEntry.time;
		singleDot.date = doubleDot.date = dirEntry.date;
		singleDot.fileSize = doubleDot.fileSize = 0;

		for (int i = 2; i < 8; i++) {
			singleDot.name[i] = doubleDot.name[i] = ' ';
		}
		char buffer[BYTES_PER_SECTOR] = { 0 };
		memcpy(buffer, &singleDot, sizeof(DirEntry));
		memcpy(buffer + sizeof(DirEntry), &doubleDot, sizeof(DirEntry));
		fmsWriteSector(buffer, C_2_S(startCluster));
	}
	else {
		char* dot = strchr(fileName, '.');
		int extLen = dot ? strlen(dot + 1) : 0;
		if (!dot) { // no dot in filename
			dot = fileName + strlen(fileName);
			extLen = 0;
		}
		for (int i = 0; i < dot - fileName; i++) {
			dirEntry.name[i] = toupper(fileName[i]);
		}
		for (int i = dot - fileName; i < 8; i++) {
			dirEntry.name[i] = ' ';
		}
		for (int i = 0; i < extLen; i++) {
			dirEntry.extension[i] = toupper(dot[i + 1]);
		}
		for (int i = extLen; i < 3; i++) {
			dirEntry.extension[i] = ' ';
		}
	}

	return writeDirEntry(&dirEntry, fileName, TRUE);
} // end fmsDefineFile


int findCluster(int currentCluster) {
	int newCluster;
	for (int i = 2; i < NUM_FAT_SECTORS * BYTES_PER_SECTOR; i++) {
		newCluster = getFatEntry(i, FAT1);
		if (newCluster == 0) {
			if (currentCluster != 0) {
				setFatEntry(currentCluster, i, FAT1);
			}
			setFatEntry(i, FAT_EOC, FAT1);
			memcpy(FAT2, FAT1, BYTES_PER_SECTOR * NUM_FAT_SECTORS);
			return i;
		}
	}
	return ERR65;
}


int writeDirEntry(DirEntry* newEntry, char* mask, bool isNew) {
	int error, dirSector, dirIndex, dirNum = 0, loop = 0;
	int currentCluster, dirCluster = CDIR;
	char buffer[BYTES_PER_SECTOR];

	while (1) {		// load directory sector
		if (CDIR) { // sub directory
			while (loop--) {
				currentCluster = dirCluster;
				dirCluster = getFatEntry(dirCluster, FAT1);
				if (dirCluster == FAT_EOC) {
					if (isNew) {
						// start using a new cluster
						int nextAvailable = findCluster(currentCluster);
						if (nextAvailable < 0)
							return nextAvailable;
						char clearBuffer[BYTES_PER_SECTOR] = { 0 };
						fmsWriteSector(clearBuffer, C_2_S(nextAvailable));
						dirCluster = nextAvailable;
					}
					else
						return ERR67;
				}
				if (dirCluster == FAT_BAD)
					return ERR54;
				if (dirCluster < 2)
					return ERR54;
			}
			dirSector = C_2_S(dirCluster);
		}
		else { // root directory
			dirSector = (dirNum / ENTRIES_PER_SECTOR) + BEG_ROOT_SECTOR;
			if (dirSector >= BEG_DATA_SECTOR)
				return ERR67;
		}

		// read sector into directory buffer
		if (error = fmsReadSector(buffer, dirSector))
			return error;

		DirEntry* dirEntry;
		// find next matching directory entry
		while (1) { // read directory entry
			dirIndex = dirNum % ENTRIES_PER_SECTOR;
			dirEntry = &buffer[dirIndex * sizeof(DirEntry)];
			if (isNew && (dirEntry->name[0] == 0 || dirEntry->name[0] == 0xe5)) {
				memcpy(dirEntry, newEntry, sizeof(DirEntry));
				fmsWriteSector(buffer, dirSector);
				return 0;
			}
			if (dirEntry->name[0] == 0) {
				return ERR67; // EOD
			}
			dirNum++;		  // prepare for next read
			if (dirEntry->name[0] == 0xe5) {
				// else Deleted entry, go on...
			}
			else if (dirEntry->attributes == LONGNAME)
				;
			else if (fmsMask(mask, dirEntry->name, dirEntry->extension) == 1) {
				memcpy(dirEntry, newEntry, sizeof(DirEntry));
				fmsWriteSector(buffer, dirSector);
				return 0;
			}
			// break if sector boundary
			if ((dirNum % ENTRIES_PER_SECTOR) == 0)
				break;
		}
		// next directory sector/cluster
		loop = 1;
	}

	return ERR68;
}



// ***********************************************************************
// ***********************************************************************
// This function deletes the file fileName from the current directory.
// The file name should be marked with an "E5" as the first character and the chained
// clusters in FAT 1 reallocated (cleared to 0).
// Return 0 for success; otherwise, return the error number.
//
int fmsDeleteFile(char* fileName) {
	int error, nextCluster, currentCluster;

	if (!isValidFileName(fileName))
		return ERR50;

	DirEntry dirEntry, fakeDir;
	error = fmsGetDirEntry(fileName, &dirEntry);
	if (error) { // possibly returns ERR61 (File Not Defined)
		return error;
	}

	if (dirEntry.attributes & DIRECTORY) {
		int address = 2;
		error = fmsGetNextDirEntry(&address, "**", &fakeDir, dirEntry.startCluster);
		if (error != ERR67)
			return ERR69;
	}

	if (dirEntry.startCluster) {
		currentCluster = dirEntry.startCluster;
		while (currentCluster != FAT_EOC) {
			nextCluster = getFatEntry(currentCluster, FAT1);
			setFatEntry(currentCluster, 0, FAT1);
			currentCluster = nextCluster;
		}
	}
	// check for if it's deletable or not
	
	dirEntry.name[0] = 0xe5;

	return writeDirEntry(&dirEntry, fileName, FALSE);
} // end fmsDeleteFile



// ***********************************************************************
// ***********************************************************************
// This function opens the file fileName for access as specified by rwMode.
// It is an error to try to open a file that does not exist.
// The open mode rwMode is defined as follows:
//    0 - Read access only.
//       The file pointer is initialized to the beginning of the file.
//       Writing to this file is not allowed.
//    1 - Write access only.
//       The file pointer is initialized to the beginning of the file.
//       Reading from this file is not allowed.
//    2 - Append access.
//       The file pointer is moved to the end of the file.
//       Reading from this file is not allowed.
//    3 - Read/Write access.
//       The file pointer is initialized to the beginning of the file.
//       Both read and writing to the file is allowed.
// A maximum of 32 files may be open at any one time.
// If successful, return a file descriptor that is used in calling subsequent file
// handling functions; otherwise, return the error number.
//
int fmsOpenFile(char* fileName, int rwMode) {
	if (!diskMounted)
		return ERR72;
	if (!isValidFileName(fileName))
		return ERR50;
	// find directory entry
	int fd = -1, error = 0;
	for (int i = 0; i < NFILES; i++) {
		if (OFTable[i].name[0] != 0) {
			if (fmsMask(fileName, OFTable[i].name, OFTable[i].extension) == 1) {
				return ERR62; // file already open
			}
		}
		else if (OFTable[i].name[0] == 0 && fd == -1) {
			fd = i;
		}
	}
	if (fd == -1) {
		return ERR70;
	}
	DirEntry* dirEntry = malloc(sizeof(DirEntry));
	error = fmsGetDirEntry(fileName, dirEntry);
	if (error) { // possibly returns ERR61 (File Not Defined)
		return error;
	}
	if (dirEntry->attributes & DIRECTORY) {
		return ERR51;
	}
	if (dirEntry->attributes & READ_ONLY && rwMode != 0) {
		return ERR84;
	}
	// possibly other error checking first
	FCB* newEntry = OFTable + fd;
	memcpy(newEntry, dirEntry, 12*sizeof(unsigned char));
	newEntry->directoryCluster = CDIR;
	newEntry->startCluster = dirEntry->startCluster;
	newEntry->currentCluster = 0;
	newEntry->fileSize = (rwMode == 1) ? 0 : dirEntry->fileSize;
	newEntry->pid = curTask;
	newEntry->mode = rwMode;
	newEntry->flags = 0;
	newEntry->fileIndex = (rwMode != 2) ? 0 : dirEntry->fileSize;

	free(dirEntry);

	return fd;
} // end fmsOpenFile



// ***********************************************************************
// ***********************************************************************
// This function reads nBytes bytes from the open file specified by fileDescriptor into
// memory pointed to by buffer.
// The fileDescriptor was returned by fmsOpenFile and is an index into the open file table.
// After each read, the file pointer is advanced.
// Return the number of bytes successfully read (if > 0) or return an error number.
// (If you are already at the end of the file, return EOF error.  ie. you should never
// return a 0.)
//
int fmsReadFile(int fileDescriptor, char* buffer, int nBytes) {
	int error, nextCluster, numBytesRead = 0;
	unsigned int bytesLeft, bufferIndex;
	FCB* fdEntry = &OFTable[fileDescriptor];
	if (fdEntry->name[0] == 0)
		return ERR63;
	if (fdEntry->mode == 1 || fdEntry->mode == 2)
		return ERR85;
	while (nBytes > 0) {
		if (fdEntry->fileSize == fdEntry->fileIndex)
			return numBytesRead ? numBytesRead : ERR66;
		bufferIndex = fdEntry->fileIndex % BYTES_PER_SECTOR;
		if (bufferIndex == 0 && (fdEntry->fileIndex || !fdEntry->currentCluster)) {
			if (fdEntry->currentCluster == 0) {
				if (fdEntry->startCluster == 0)
					return ERR66;
				nextCluster = fdEntry->startCluster;
				fdEntry->fileIndex = 0;
			}
			else {
				nextCluster = getFatEntry(fdEntry->currentCluster, FAT1);
				if (nextCluster == FAT_EOC)
					return numBytesRead;
			}

			if (fdEntry->flags & BUFFER_ALTERED) {
				if ((error = fmsWriteSector(fdEntry->buffer, C_2_S(fdEntry->currentCluster))))
					return error;
				fdEntry->flags &= ~BUFFER_ALTERED;
			}
			if ((error = fmsReadSector(fdEntry->buffer, C_2_S(nextCluster))))
				return error;
			fdEntry->currentCluster = nextCluster;
		}
		bytesLeft = BYTES_PER_SECTOR - bufferIndex;
		if (bytesLeft > nBytes)
			bytesLeft = nBytes;
		int newSize = fdEntry->fileSize - fdEntry->fileIndex;
		if (bytesLeft > newSize)
			bytesLeft = newSize;
		memcpy(buffer, &fdEntry->buffer[bufferIndex], bytesLeft);
		fdEntry->fileIndex += bytesLeft;
		numBytesRead += bytesLeft;
		buffer += bytesLeft;
		nBytes -= bytesLeft;
	}

	return numBytesRead;
} // end fmsReadFile



// ***********************************************************************
// ***********************************************************************
// This function changes the current file pointer of the open file specified by
// fileDescriptor to the new file position specified by index.
// The fileDescriptor was returned by fmsOpenFile and is an index into the open file table.
// The file position may not be positioned beyond the end of the file.
// Return the new position in the file if successful; otherwise, return the error number.
//
int fmsSeekFile(int fileDescriptor, int index) {
	int error, currentCluster, nextCluster, oldCluster, clusterNum;
	if (fileDescriptor < 0 || fileDescriptor >= NFILES)
		return ERR52;
	FCB* fdEntry = &OFTable[fileDescriptor];
	if (fdEntry->name[0] == 0)
		return ERR63;
	if (index < 0 || index > fdEntry->fileSize)
		return ERR80;
	if ((fdEntry->mode == 1) || (fdEntry->mode == 2))
		return ERR85;

	oldCluster = fdEntry->currentCluster;
	currentCluster = fdEntry->startCluster;
	
	clusterNum = index / BYTES_PER_SECTOR;
	if (index % BYTES_PER_SECTOR == 0)
		clusterNum--;
	for (int i = 0; i < clusterNum; i++) {
		nextCluster = getFatEntry(currentCluster, FAT1);
		currentCluster = nextCluster;
	}
	fdEntry->currentCluster = currentCluster;
	fdEntry->fileIndex = index;
	if (oldCluster != currentCluster) {
		if (fdEntry->flags & BUFFER_ALTERED)
			fmsWriteSector(fdEntry->buffer, C_2_S(oldCluster));
		if ((error = fmsReadSector(fdEntry->buffer, C_2_S(currentCluster))))
			return error;
	}

	return index;
} // end fmsSeekFile



// ***********************************************************************
// ***********************************************************************
// This function writes nBytes bytes to the open file specified by fileDescriptor from
// memory pointed to by buffer.
// The fileDescriptor was returned by fmsOpenFile and is an index into the open file table.
// Writing is always "overwriting" not "inserting" in the file and always writes forward
// from the current file pointer position.
// Return the number of bytes successfully written; otherwise, return the error number.
//
int fmsWriteFile(int fileDescriptor, char* buffer, int nBytes) {
	int error, nextCluster, numBytesWritten = 0;
	unsigned int writeBytes, bufferIndex;
	FCB* fdEntry = &OFTable[fileDescriptor];
	if (fdEntry->name[0] == 0)
		return ERR63;
	if (fdEntry->mode == 0)
		return ERR85;

	while (nBytes > 0) {
		bufferIndex = fdEntry->fileIndex % BYTES_PER_SECTOR;
		if (bufferIndex == 0 && fdEntry->fileIndex) {
			nextCluster = getFatEntry(fdEntry->currentCluster, FAT1);
			if (nextCluster == FAT_EOC) {
				int nextAvailable = findCluster(fdEntry->currentCluster);
				if (nextAvailable < 0)
					return nextAvailable;
				nextCluster = nextAvailable;
			}
			if (fdEntry->flags & BUFFER_ALTERED)
				fmsWriteSector(fdEntry->buffer, C_2_S(fdEntry->currentCluster));

			fdEntry->currentCluster = nextCluster;

			if ((error = fmsReadSector(fdEntry->buffer, C_2_S(fdEntry->currentCluster))))
				return error;

			fdEntry->flags &= ~BUFFER_ALTERED;
		}
		
		writeBytes = BYTES_PER_SECTOR - bufferIndex;
		if (writeBytes > nBytes)
			writeBytes = nBytes;

		memcpy(&fdEntry->buffer[bufferIndex], buffer, writeBytes);
		fdEntry->fileIndex += writeBytes;
		numBytesWritten += writeBytes;
		buffer += writeBytes;
		nBytes -= writeBytes;
		fdEntry->flags |= BUFFER_ALTERED;

		if (fdEntry->currentCluster == 0) {
			if (fdEntry->startCluster == 0) {
				int nextAvailable = findCluster(0);
				if (nextAvailable < 0)
					return nextAvailable;
				fdEntry->startCluster = nextAvailable;
			}
			fdEntry->currentCluster = fdEntry->startCluster;
		}

		if (nBytes && writeBytes) {
			if ((error = fmsWriteSector(fdEntry->buffer, C_2_S(fdEntry->currentCluster))))
				return error;
			fdEntry->flags &= ~BUFFER_ALTERED;
		}
	}

	fdEntry->fileSize += numBytesWritten;
	fdEntry->flags |= FILE_ALTERED;

	return numBytesWritten;
} // end fmsWriteFile
