// os345.c - OS Kernel	06/21/2020
// ***********************************************************************
// **   DISCLAMER ** DISCLAMER ** DISCLAMER ** DISCLAMER ** DISCLAMER   **
// **                                                                   **
// ** The code given here is the basis for the BYU CS345 projects.      **
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

//#include "stdafx.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <setjmp.h>
#include <time.h>
#include <assert.h>
#include <windows.h>

#include "os345.h"
#include "os345signals.h"
#include "os345config.h"
#include "os345lc3.h"
#include "os345fat.h"

// to enable ansi
#ifndef ENABLE_VIRTUAL_TERMINAL_PROCESSING
#define ENABLE_VIRTUAL_TERMINAL_PROCESSING 0x0004
#endif

// **********************************************************************
//	local prototypes
//
void setupConsole(void);
void pollInterrupts(void);
static int scheduler(void);
static int dispatcher(void);

//static void keyboard_isr(void);
//static void timer_isr(void);

int sysKillTask(int taskId);
static int initOS(void);

// **********************************************************************
// **********************************************************************
// global semaphores

Semaphore* semaphoreList; // linked list of active semaphores

Semaphore* keyboard;	  // keyboard semaphore
Semaphore* charReady;	  // character has been entered
Semaphore* inBufferReady; // input buffer ready semaphore

Semaphore* tics1sec;	// 1 second semaphore
Semaphore* tics10thsec; // 1/10 second semaphore
Semaphore* tics10sec;

// **********************************************************************
// **********************************************************************
// global system variables

TCB tcb[MAX_TASKS];				// task control block
Semaphore* taskSems[MAX_TASKS]; // task semaphore
jmp_buf k_context;				// context of kernel stack
jmp_buf reset_context;			// context of kernel stack
volatile void* temp;			// temp pointer used in dispatcher

int scheduler_mode;			   // scheduler mode
int superMode;				   // system mode
int curTask;				   // current task #
long swapCount;				   // number of re-schedule cycles
char inChar;				   // last entered character
int charFlag;				   // 0 => buffered input
int inBufIndx;				   // input pointer into input buffer
char inBuffer[INBUF_SIZE + 1]; // character input buffer
//Message messages[NUM_MESSAGES];		// process message buffers

int pollClock;	   // current clock()
int lastPollClock; // last pollClock
bool diskMounted;  // disk has been mounted

time_t oldTime1; // old 1sec time
time_t oldTime10;
clock_t myClkTime;
clock_t myOldClkTime;
PQueue rq; // ready priority queue
DeltaClock dcHead = NULL; // deltaclock
Semaphore* dcMutex;
int deltaTime = 0;

extern Semaphore* dcChange; // testing only

static HANDLE stdoutHandle;
static DWORD outModeInit;

#define TOTAL_TIME 6000


// **********************************************************************
// **********************************************************************
// OS startup
//
// 1. Init OS
// 2. Define reset longjmp vector
// 3. Define global system semaphores
// 4. Create CLI task
// 5. Enter scheduling/idle loop
//
int main(int argc, char* argv[]) {
	// save context for restart (a system reset would return here...)
	int resetCode = setjmp(reset_context);
	superMode = TRUE; // supervisor mode

	switch (resetCode) {
	case POWER_DOWN_QUIT: // quit
		powerDown(0);
		printf("Goodbye!!\n");
		return 0;

	case POWER_DOWN_RESTART: // restart
		powerDown(resetCode);
		printf("\nRestarting system...\n");

	case POWER_UP: // startup
		break;

	default:
		printf("Shutting down due to error %d\n", resetCode);
		powerDown(resetCode);
		return resetCode;
	}

	// output header message
	printf("%s", STARTUP_MSG);

	// initalize OS
	if (resetCode = initOS())
		return resetCode;

	// create global/system semaphores here
	//?? vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

	charReady = createSemaphore("charReady", BINARY, 0);
	inBufferReady = createSemaphore("inBufferReady", BINARY, 0);
	keyboard = createSemaphore("keyboard", BINARY, 1);
	tics1sec = createSemaphore("tics1sec", BINARY, 0);
	tics10thsec = createSemaphore("tics10thsec", BINARY, 0);
	tics10sec = createSemaphore("tics10sec", COUNTING, 0);

	//?? ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

	// schedule CLI task
	createTask("myShell", // task name
		P1_main,		  // task
		MED_PRIORITY,	  // task priority
		argc,			  // task arg count
		argv);			  // task argument pointers

	// HERE WE GO................

	// Scheduling loop
	// 1. Check for asynchronous events (character inputs, timers, etc.)
	// 2. Choose a ready task to schedule
	// 3. Dispatch task
	// 4. Loop (forever!)

	while (1) // scheduling loop
	{
		// check for character / timer interrupts
		pollInterrupts();

		// schedule highest priority ready task
		if ((curTask = scheduler()) < 0)
			continue;

		// dispatch curTask, quit OS if negative return
		if (dispatcher() < 0)
			break;
	} // end of scheduling loop

	// exit os
	longjmp(reset_context, POWER_DOWN_QUIT);
	return 0;
} // end main


int enQ(PQueue q, TID tid, Priority p) {
	// start at 1 to skip the arg checker, numArgs stored in the "prio" property of q[0]
	Tuple newTask = { p, tid };
	if (q[0].prio == 0) {
		q[1] = newTask;
		q[0].prio++;
	}
	else if (q[0].prio == MAX_TASKS) {
		printf("\nMaximum number of tasks already created. Kill at least one task and try again.\n");
		return -1; // the right thing to return here?
	}
	else {
		for (int i = 1; i < MAX_TASKS; i++) {
			if (i > q[0].prio || q[i].prio < p) {
				q[0].prio++;
				for (int j = q[0].prio; j > i; j--) {
					q[j] = q[j - 1];
				}
				q[i] = newTask;
				break;
			}
		}
	}
	return tid;
}

int PQueue_pop(PQueue q, int index) {
	if (q[0].prio == 0) {
		printf("\nThere are currently no tasks in the queue.\n");
		return -1;
	}

	int ret = q[index].tid;

	for (int i = index; i < q[0].prio; i++) {
		q[i] = q[i + 1];
	}
	q[q[0].prio] = (Tuple){ '\0', '\0' };
	q[0].prio--;

	return ret;
}

int deQ(PQueue q, TID tid) {
	int deletedTID;
	if (q[0].prio == 0)
		return -1;
	if (tid == -1) {
		return PQueue_pop(q, 1); // just do the first one
	}
	for (int i = 1; i <= q[0].prio; i++) {
		if (q[i].tid == tid)
			return PQueue_pop(q, i);
	}

	return -1; // tid wasn't found
}

void dc_insert(Node* node) {
	SEM_WAIT(dcMutex); SWAP;
	if (dcHead == NULL) {
		dcHead = node; SWAP;
		node->next = NULL; SWAP;
		SEM_SIGNAL(dcMutex); SWAP;
		return;
	}
	Node* curNode = dcHead; SWAP;
	Node* prevNode = NULL; SWAP;
	while (curNode && node->tickTime > curNode->tickTime) {
		node->tickTime -= curNode->tickTime; SWAP;
		prevNode = curNode; SWAP;
		curNode = curNode->next; SWAP;
	}
	// curNode->tickTime > node->tickTime at this point or curNode is NULL
	node->next = curNode; SWAP;
	if (prevNode) {
		prevNode->next = node; SWAP;
	}
	else {
		dcHead = node; SWAP;
	}
	if (curNode) {
		curNode->tickTime -= node->tickTime; SWAP;
	}
	SEM_SIGNAL(dcMutex); SWAP;
}

void dc_tick() {
	Node* temp;

	if (dcHead) {
		deltaTime += 1;
	}

	while (dcHead && dcHead->tickTime <= deltaTime) {
		deltaTime = 0;
		temp = dcHead;
		dcHead = dcHead->next;
		SEM_SIGNAL(temp->sem);
		free(temp);
		SEM_SIGNAL(dcChange);
	}
}

void calculate_time_share() {
	TID parents[NUM_PARENTS + 1] = { 0 }; // remember the shell
	int curr_parent = 0, curr_child = 0;
	// set up parents
	for (int i = 1; i <= rq[0].prio; i++) {
		if (tcb[rq[i].tid].parent == 0) {
			parents[curr_parent] = rq[i].tid;
			curr_parent++;
		}
	}
	// set up children of parents and allot time
	int family_time = TOTAL_TIME / (NUM_PARENTS + 1); // time allotment to each parental group
	for (int i = 0; i < curr_parent; i++) {
		int num_children = 0;
		for (int j = 1; j <= rq[0].prio; j++) {
			if (tcb[rq[j].tid].parent == parents[i]) {
				num_children++;
			}
		}

		int group_time = family_time / (num_children + 1);
		for (int j = 1; j <= rq[0].prio; j++) {
			if (tcb[rq[j].tid].parent == parents[i]) {
				tcb[rq[j].tid].time = group_time;
			}
		}
		tcb[parents[i]].time = group_time + (family_time % (num_children + 1)); // if num_children is zero, group time won't add anything
	}
}

int deQ_time_item(PQueue q) {
	if (q[0].prio == 0)
		return -1;
	for (int i = 1; i <= q[0].prio; i++) {
		if (tcb[q[i].tid].time > 0) {
			return PQueue_pop(q, i);
		}
	}
	// everything has a time of zero, recalculate
	calculate_time_share(); // this is slowing down the scheduling of tasks
	return PQueue_pop(q, 1);
}


// **********************************************************************
// **********************************************************************
// scheduler
// DO NOT CALL SWAPTASK HERE
//
static int scheduler() {
	int nextTask;

	// schedule next task
	if ( (nextTask = (scheduler_mode == 0 ? deQ(rq, -1) : deQ_time_item(rq)) ) >= 0) {
		if (scheduler_mode != 0) {
			tcb[nextTask].time--;
		}
		enQ(rq, nextTask, tcb[nextTask].priority);
	}

	// make sure nextTask is valid
	while (!tcb[nextTask].name) {
		if (++nextTask >= MAX_TASKS)
			nextTask = 0;
	}
	if (tcb[nextTask].signal & mySIGSTOP)
		return -1;

	return nextTask;
} // end scheduler



// **********************************************************************
// **********************************************************************
// dispatch curTask
//
static int dispatcher() {
	int result;

	// schedule task
	switch (tcb[curTask].state) {
	case S_NEW: {
		// new task
		printf("\nNew Task[%d] %s\n", curTask, tcb[curTask].name);
		tcb[curTask].state = S_RUNNING; // set task to run state

		// save kernel context for task SWAP's
		if (setjmp(k_context)) {
			superMode = TRUE; // supervisor mode
			break;			  // context switch to next task
		}

		// move to new task stack (leave room for return value/address)
		temp = (int*)tcb[curTask].stack + (STACK_SIZE - 8);
		SET_STACK(temp);
		superMode = FALSE; // user mode

		// begin execution of new task, pass argc, argv
		result = (*tcb[curTask].task)(tcb[curTask].argc, tcb[curTask].argv);

		// task has completed
		if (result)
			printf("\nTask[%d] returned error %d\n", curTask, result);
		else
			printf("\nTask[%d] returned %d\n", curTask, result);
		tcb[curTask].state = S_EXIT; // set task to exit state

		// return to kernal mode
		longjmp(k_context, 1); // return to kernel
	}

	case S_READY: {
		tcb[curTask].state = S_RUNNING; // set task to run
	}

	case S_RUNNING: {
		if (setjmp(k_context)) {
			// SWAP executed in task
			superMode = TRUE; // supervisor mode
			break;			  // return from task
		}
		if (signals())
			break;
		longjmp(tcb[curTask].context, 3); // restore task context
	}

	case S_BLOCKED: {
		break;
	}

	case S_EXIT: {
		if (curTask == 0)
			return -1; // if CLI, then quit scheduler
		// release resources and kill task
		sysKillTask(curTask); // kill current task
		break;
	}

	default: {
		printf("\nUnknown Task[%d] State\n", curTask);
		longjmp(reset_context, POWER_DOWN_ERROR);
	}
	}
	return 0;
} // end dispatcher



// **********************************************************************
// **********************************************************************
// Do a context switch to next task.

// 1. If scheduling task, return (setjmp returns non-zero value)
// 2. Else, save current task context (setjmp returns zero value)
// 3. Set current task state to READY
// 4. Enter kernel mode (longjmp to k_context)

void swapTask() {
	assert("SWAP Error" && !superMode); // assert user mode

	// increment swap cycle counter
	swapCount++;

	// either save current task context or schedule task (return)
	if (setjmp(tcb[curTask].context)) {
		superMode = FALSE; // user mode
		return;
	}

	// context switch - move task state to ready
	if (tcb[curTask].state == S_RUNNING)
		tcb[curTask].state = S_READY;

	// move to kernel mode (reschedule)
	longjmp(k_context, 2);
} // end swapTask


void setupConsole(void) {
	DWORD outMode = 0;
	stdoutHandle = GetStdHandle(STD_OUTPUT_HANDLE);

	if (stdoutHandle == INVALID_HANDLE_VALUE) {
		exit(GetLastError());
	}

	if (!GetConsoleMode(stdoutHandle, &outMode)) {
		exit(GetLastError());
	}
	outModeInit = outMode;

	// Enable ANSI escape codes
	outMode |= ENABLE_VIRTUAL_TERMINAL_PROCESSING;

	if (!SetConsoleMode(stdoutHandle, outMode)) {
		exit(GetLastError());
	}
}



// **********************************************************************
// **********************************************************************
// system utility functions
// **********************************************************************
// **********************************************************************

// **********************************************************************
// **********************************************************************
// initialize operating system
static int initOS() {
	int i;

	// make any system adjustments (for unblocking keyboard inputs)
	INIT_OS

	setupConsole();

	// reset system variables
	curTask = 0;		// current task #
	swapCount = 0;		// number of scheduler cycles
	scheduler_mode = 0; // default scheduler
	inChar = 0;			// last entered character
	charFlag = 0;		// 0 => buffered input
	inBufIndx = 0;		// input pointer into input buffer
	semaphoreList = 0;	// linked list of active semaphores
	diskMounted = 0;	// disk has been mounted

	// malloc ready queue
	rq = (PQueue)malloc(MAX_TASKS * sizeof(Tuple));
	if (rq == NULL)
		return 99;
	rq[0] = (Tuple) {0, 0};

	dcMutex = createSemaphore("dcMutex", BINARY, S_READY);

	// capture current time
	lastPollClock = clock(); // last pollClock
	time(&oldTime1);
	time(&oldTime10);

	// init system tcb's
	for (i = 0; i < MAX_TASKS; i++) {
		tcb[i].name = NULL; // tcb
		taskSems[i] = NULL; // task semaphore
	}

	// init tcb
	for (i = 0; i < MAX_TASKS; i++) {
		tcb[i].name = NULL;
	}

	// initialize lc-3 memory
	initLC3Memory(LC3_MEM_FRAME, 0xF800 >> 6);

	// ?? initialize all execution queues

	return 0;
} // end initOS



// **********************************************************************
// **********************************************************************
// Causes the system to shut down. Use this for critical errors
void powerDown(int code) {
	int i;
	printf("PowerDown Code %d\n", code);

	// release all system resources.
	printf("\nRecovering Task Resources...\n");

	// kill all tasks
	for (i = MAX_TASKS - 1; i >= 0; i--)
		if (tcb[i].name)
			sysKillTask(i);

	// delete all semaphores
	while (semaphoreList)
		deleteSemaphore(&semaphoreList);

	// free ready queue
	free(rq);

	// ?? release any other system resources
	// ?? deltaclock (project 3)

	RESTORE_OS
	return;
} // end powerDown
