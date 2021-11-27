// os345p3.c - Jurassic Park 07/27/2020
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
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <setjmp.h>
#include <time.h>
#include <assert.h>
#include "os345.h"
#include "os345park.h"

// ***********************************************************************
// project 3 variables

// Jurassic Park
extern JPARK myPark;
extern Semaphore* parkMutex;			// protect park access
extern Semaphore* fillSeat[NUM_CARS];	// (signal) seat ready to fill
extern Semaphore* seatFilled[NUM_CARS]; // (wait) passenger seated
extern Semaphore* rideOver[NUM_CARS];	// (signal) ride over

extern TCB tcb[];
extern DeltaClock dcHead;

Semaphore* jurassicComm; // "mailbox" thing, intertask communication
int holderID; // also mailboxy
Semaphore* commMutex; // communication mutex
Semaphore* commSent; // send comm
Semaphore* commReceived; // wait for comm to be there
Semaphore* getPassenger;
Semaphore* needPassenger;
Semaphore* seatTaken;
Semaphore* passengerSeated;
Semaphore* needDriver;
Semaphore* needDriverMutex;
Semaphore* wakeupDriver;

Semaphore* parkVisitors; // restrict to MAX_IN_PARK
Semaphore* parkTickets;	// restrict to MAX_TICKETS
Semaphore* parkMuseum; // restrict to MAX_IN_MUSEUM
Semaphore* parkGiftShop; // restrict to MAX_IN_GIFTSHOP
Semaphore* ticketMutex;
Semaphore* needTicket;
Semaphore* takeTicket;
Semaphore* driverReady;
Semaphore* carReady;

TID timeTaskID; // testing only
Semaphore* event[10]; // testing only
Semaphore* dcChange; // testing only


// ***********************************************************************
// project 3 functions and tasks
void CL3_project3(int, char**);
void CL3_dc(int, char**);
void printDeltaClock(void);
int dcMonitorTask(int argc, char* argv[]);
int timeTask(int argc, char* argv[]);
void visitorTask(int argc, char* argv[]);
void carTask(int argc, char* argv[]);
void driverTask(int argc, char* argv[]);
void createAddNode(int tickTime, Semaphore* timer);


// ***********************************************************************
// ***********************************************************************
// project3 command
int P3_main(int argc, char* argv[]) {
	char buf[32];
	char* newArgv[2];
	parkMutex = NULL;
	jurassicComm = NULL;
	commMutex = createSemaphore("commMutex", BINARY, 1);
	commSent = createSemaphore("commSent", BINARY, 0);
	commReceived = createSemaphore("commReceived", BINARY, 0);
	getPassenger = createSemaphore("getPassenger", BINARY, 0);
	needPassenger = createSemaphore("needPassenger", BINARY, 0);
	seatTaken = createSemaphore("seatTaken", BINARY, 0);
	passengerSeated = createSemaphore("passengerSeated", BINARY, 0);
	needDriver = createSemaphore("needDriver", BINARY, 0);
	needDriverMutex = createSemaphore("needDriverMutex", BINARY, 1);
	wakeupDriver = createSemaphore("wakeupDriver", BINARY, 0);
	ticketMutex = createSemaphore("ticketMutex", BINARY, 1);
	needTicket = createSemaphore("needTicket", BINARY, 0);
	takeTicket = createSemaphore("takeTicket", BINARY, 0);
	driverReady = createSemaphore("driverReady", BINARY, 0);
	carReady = createSemaphore("carReady", BINARY, 0);

	dcChange = createSemaphore("dcChange", BINARY, 0);
	// resource semaphores
	parkVisitors = createSemaphore("parkVisitors", COUNTING, MAX_IN_PARK);
	parkTickets = createSemaphore("parkTickets", COUNTING, MAX_TICKETS);
	parkMuseum = createSemaphore("parkMuseum", COUNTING, MAX_IN_MUSEUM);
	parkGiftShop = createSemaphore("parkGiftShop", COUNTING, MAX_IN_GIFTSHOP);

	// start park
	sprintf(buf, "jurassicPark");
	newArgv[0] = buf;
	createTask(buf,	  // task name
		jurassicTask, // task
		MED_PRIORITY, // task priority
		1,			  // task count
		newArgv);	  // task argument

	// wait for park to get initialized...
	while (!parkMutex)
		SWAP;
	printf("\nStart Jurassic Park...");

	//?? create car, driver, and visitor tasks here
	for (int i = 0; i < NUM_CARS; i++) {
		char name[16], carID[16], *carArgv[2];
		sprintf(name, "carTask%d", i);
		sprintf(carID, "%d", i);
		carArgv[0] = name;
		carArgv[1] = carID;
		createTask(name,
			carTask,
			MED_PRIORITY,
			2,
			carArgv);
	}

	for (int i = 0; i < NUM_VISITORS; i++) {
		char name[24], visitorID[16], *visitorArgv[2];
		sprintf(name, "visitorTask%d", i);
		sprintf(visitorID, "%d", i);
		visitorArgv[0] = name;
		visitorArgv[1] = visitorID;
		createTask(name,
			visitorTask,
			MED_PRIORITY,
			2,
			visitorArgv);
	}

	for (int i = 0; i < NUM_DRIVERS; i++) {
		char name[16], driverID[16], *driverArgv[2];
		sprintf(name, "driverTask%d", i);
		sprintf(driverID, "%d", i);
		driverArgv[0] = name;
		driverArgv[1] = driverID;
		createTask(name,
			driverTask,
			MED_PRIORITY,
			2,
			driverArgv);
	}

	return 0;
} // end project3


void visitorTask(int argc, char* argv[]) {
	int visitorID = atoi(argv[1]); SWAP;
	char timerSem[24], rideOverSem[24]; SWAP;
	sprintf(timerSem, "visitorTimer%d", visitorID); SWAP;
	sprintf(rideOverSem, "visitorRideOver%d", visitorID); SWAP;
	Semaphore* timer = createSemaphore(timerSem, BINARY, 0); SWAP;
	Semaphore* rideOver = createSemaphore(rideOverSem, BINARY, 0); SWAP;

	// arrive at park
	createAddNode(rand() % MAX_PARK_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;

	SEM_WAIT(parkMutex); SWAP;
	myPark.numOutsidePark++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;

	// enter park timer (10 seconds)
	createAddNode(rand() % MAX_LINE_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;
	SEM_WAIT(parkVisitors); SWAP; // wait for a spot in the park

	//upon being allowed in the park, a visitor must get in line to purchase a ticket.
	SEM_WAIT(parkMutex); SWAP;
	myPark.numOutsidePark--; SWAP;
	myPark.numInPark++; SWAP;
	myPark.numInTicketLine++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;

	// buy ticket timer (3 seconds)
	createAddNode(rand() % MAX_LINE_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;

	// buy ticket from driver
	SEM_WAIT(ticketMutex); SWAP;
	SEM_SIGNAL(needTicket); SWAP;
	SEM_SIGNAL(wakeupDriver); SWAP;
	SEM_WAIT(takeTicket); SWAP;
	SEM_SIGNAL(ticketMutex); SWAP;

	SEM_WAIT(parkMutex); SWAP;
	myPark.numTicketsAvailable--; SWAP;
	myPark.numInTicketLine--; SWAP;
	myPark.numInMuseumLine++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;

	//after successfully obtaining a ticket from a free driver, the visitor gets in the museum line. (3 seconds)
	createAddNode(rand() % MAX_LINE_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;

	SEM_WAIT(parkMuseum); SWAP;

	SEM_WAIT(parkMutex); SWAP;
	myPark.numInMuseumLine--; SWAP;
	myPark.numInMuseum++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;

	// visit museum (3 seconds)
	createAddNode(rand() % MAX_LINE_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;

	//after visiting the museum, the visitor gets in the tour car line to wait until permitted to board a tour car.
	SEM_WAIT(parkMutex); SWAP;
	myPark.numInMuseum--; SWAP;
	myPark.numInCarLine++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;
	SEM_SIGNAL(parkMuseum); SWAP;

	// wait in car line
	createAddNode(rand() % MAX_LINE_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;
	SEM_WAIT(getPassenger); SWAP;
	SEM_SIGNAL(seatTaken); SWAP;

	SEM_WAIT(commMutex); SWAP;
	//SEM_WAIT(needPassenger); SWAP;
	jurassicComm = rideOver; SWAP;
	SEM_SIGNAL(commSent); SWAP;
	SEM_WAIT(commReceived); SWAP;
	SEM_SIGNAL(commMutex); SWAP;

	SEM_WAIT(passengerSeated); SWAP;
	SEM_WAIT(parkMutex); SWAP;
	myPark.numInCarLine--; SWAP;
	myPark.numInCars++; SWAP;
	myPark.numTicketsAvailable++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;
	SEM_SIGNAL(parkTickets); SWAP;

	//when the touring car ride is over, the visitor moves to the gift shop line.
	SEM_WAIT(rideOver); SWAP;
	SEM_WAIT(parkMutex); SWAP;
	myPark.numInCars--; SWAP;
	myPark.numInGiftLine++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;

	// wait in giftshop lines
	createAddNode(rand() % MAX_LINE_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;
	SEM_WAIT(parkGiftShop); SWAP;
	SEM_WAIT(parkMutex); SWAP;
	myPark.numInGiftLine--; SWAP;
	myPark.numInGiftShop++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;

	// visit giftshop
	createAddNode(rand() % MAX_LINE_WAIT, timer); SWAP;
	SEM_WAIT(timer); SWAP;

	//after visiting the gift shop, the visitor exits the park allowing another visitor into the park.
	SEM_WAIT(parkMutex); SWAP;
	myPark.numInGiftShop--; SWAP;
	myPark.numInPark--; SWAP;
	myPark.numExitedPark++; SWAP;
	SEM_SIGNAL(parkMutex); SWAP;
	SEM_SIGNAL(parkGiftShop); SWAP;
	SEM_SIGNAL(parkVisitors); SWAP;
}

void carTask(int argc, char* argv[]) {
	int carID = atoi(argv[1]); SWAP;
	Semaphore* passengerRideOver[3]; SWAP;
	Semaphore* driverRideOver = NULL; SWAP;

	do {
		for (int i = 0; i < NUM_SEATS; i++) {
			SEM_WAIT(fillSeat[carID]); SWAP;
			SEM_SIGNAL(getPassenger); SWAP; // signal for visitor
			SEM_WAIT(seatTaken); SWAP;

			//// save passenger "ride over" semaphore
			//SEM_SIGNAL(needPassenger); SWAP;
			SEM_WAIT(commSent); SWAP;
			passengerRideOver[i] = jurassicComm; SWAP;
			SEM_SIGNAL(commReceived); SWAP;

			SEM_SIGNAL(passengerSeated); SWAP;

			if (i == 2) {
				SEM_WAIT(needDriverMutex); SWAP;
				SEM_SIGNAL(needDriver); SWAP;
				SEM_SIGNAL(wakeupDriver); SWAP;

				SEM_WAIT(commMutex); SWAP;
				holderID = carID; SWAP;
				SEM_SIGNAL(commSent); SWAP;
				SEM_WAIT(commReceived); SWAP;
				SEM_SIGNAL(commMutex); SWAP;

				// save driver "ride over" semaphore
				SEM_WAIT(commSent); SWAP;
				driverRideOver = jurassicComm; SWAP;
				SEM_SIGNAL(commReceived); SWAP;

				SEM_SIGNAL(needDriverMutex); SWAP;
			}
			SEM_SIGNAL(seatFilled[carID]); SWAP;
		}
		SEM_SIGNAL(carReady); SWAP;
		SEM_WAIT(rideOver[carID]); SWAP;
		// release passengers and driver
		SEM_SIGNAL(driverRideOver); SWAP;
		for (int i = 0; i < NUM_SEATS; i++) {
			SEM_SIGNAL(passengerRideOver[i]); SWAP;
		}
	} while (myPark.numExitedPark < NUM_VISITORS);
}

void driverTask(int argc, char* argv[]) {
	char buf[32]; SWAP;
	Semaphore* driverDone; SWAP;
	int driverID = atoi(argv[1]); SWAP;
	//printf("Starting driverTask%d", driverID);
	sprintf(buf, "driverDone%d", driverID); SWAP;
	driverDone = createSemaphore(buf, BINARY, 0); SWAP;

	while (1) {
		SEM_WAIT(wakeupDriver); SWAP;
		if (semTryLock(needDriver)) {
			// wait for carID
			SEM_WAIT(commSent); SWAP;
			int carID = holderID; SWAP;
			SEM_SIGNAL(commReceived); SWAP;

			SEM_WAIT(parkMutex); SWAP;
			myPark.drivers[driverID] = carID + 1; SWAP; // because 1-indexed
			SEM_SIGNAL(parkMutex); SWAP;

			SEM_WAIT(commMutex); SWAP;
			jurassicComm = driverDone; SWAP;
			SEM_SIGNAL(commSent); SWAP;
			SEM_WAIT(commReceived); SWAP;
			SEM_SIGNAL(commMutex); SWAP;

			SEM_SIGNAL(driverReady); SWAP;
			SEM_WAIT(carReady); SWAP;
			SEM_WAIT(driverDone); SWAP;

			SEM_WAIT(parkMutex); SWAP;
			myPark.drivers[driverID] = 0; SWAP; // sleeping driver
			SEM_SIGNAL(parkMutex); SWAP;
		}
		else if (semTryLock(needTicket)) {
			SEM_WAIT(parkMutex); SWAP;
			myPark.drivers[driverID] = -1; SWAP; // selling a ticket
			SEM_SIGNAL(parkMutex); SWAP;

			SEM_WAIT(parkTickets); SWAP;
			SEM_SIGNAL(takeTicket); SWAP;

			SEM_WAIT(parkMutex); SWAP;
			myPark.drivers[driverID] = 0; SWAP; // sleeping driver
			SEM_SIGNAL(parkMutex); SWAP;
		}
		else break;
	}
}

void createAddNode(int tickTime, Semaphore* timer) {
	Node* newNode = (Node*)malloc(sizeof(Node)); SWAP;
	newNode->tickTime = tickTime; SWAP;
	newNode->sem = timer; SWAP;
	newNode->next = NULL; SWAP;
	dc_insert(newNode); SWAP;
}


// ***********************************************************************
// ***********************************************************************
// delta clock command
int P3_dc(int argc, char* argv[]) {
	printDeltaClock();
	return 0;
} // end CL3_dc


// ***********************************************************************/
// display all pending events in the delta clock list
void printDeltaClock(void) {
	Node* curNode = dcHead;
	int i = 0;
	while (curNode) {
		printf("\n%4d%4d  %-20s", i++, curNode->tickTime, curNode->sem->name);
		curNode = curNode->next;
	}
	return;
}


// ***********************************************************************/
// test delta clock
int P3_tdc(int argc, char* argv[]) {
	dcChange = createSemaphore("dcChange", BINARY, S_NEW);

	createTask("DC Test", // task name
		dcMonitorTask,	  // task
		10,				  // task priority
		argc,			  // task arguments
		argv);

	timeTaskID = createTask("Time", // task name
		timeTask,					// task
		10,							// task priority
		argc,						// task arguments
		argv);
	return 0;
} // end P3_tdc



// ***********************************************************************/
// monitor the delta clock task
int dcMonitorTask(int argc, char* argv[]) {
	int i, flag;
	char buf[32];
	// create some test times for event[0-9]
	int ttime[10] = {
		90, 300, 50, 170, 340, 300, 50, 300, 40, 110
	};

	for (i = 0; i < 10; i++) {
		sprintf(buf, "event[%d]", i);
		event[i] = createSemaphore(buf, BINARY, 0);
		printf("adding event[%d]\n", i);
		createAddNode(ttime[i], event[i]);
	}
	printDeltaClock();

	while (dcHead) {
		SEM_WAIT(dcChange)
		flag = 0;
		for (i = 0; i < 10; i++) {
			if (event[i]->state == 1) {
				printf("\n  event[%d] signaled", i);
				event[i]->state = 0;
				flag = 1;
			}
		}
		if (flag)
			printDeltaClock();
	}
	printf("\nNo more events in Delta Clock");

	// kill dcMonitorTask
	tcb[timeTaskID].state = S_EXIT;
	return 0;
} // end dcMonitorTask


extern Semaphore* tics1sec;

// ********************************************************************************************//
// display time every tics1sec
int timeTask(int argc, char* argv[]) {
	char svtime[64]; // ascii current time
	while (1) {
		SEM_WAIT(tics1sec)
		printf("\nTime = %s", myTime(svtime));
	}
	return 0;
} // end timeTask
