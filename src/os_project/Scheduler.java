package os_project;

import java.util.List;
import java.util.LinkedList;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Collections;
import java.util.HashMap;

public class Scheduler {

	private class Interrupt extends TimerTask {
		private Scheduler scheduler;

		public Interrupt(Scheduler scheduler) {
			this.scheduler = scheduler;
		}

		@Override
		public void run() {
			scheduler.switchProcess();
		}
	}

	private List<KernelandProcess> realTimeProcessList = new LinkedList<KernelandProcess>();
	private List<KernelandProcess> interactiveProcessList = new LinkedList<KernelandProcess>();
	private List<KernelandProcess> backgroundProcessList = new LinkedList<KernelandProcess>();
	private List<KernelandProcess> sleepingProcessList = new LinkedList<KernelandProcess>();
	private HashMap<Integer, KernelandProcess> processPids = new HashMap<>();
	private HashMap<Integer, KernelandProcess> waitingForMessage = new HashMap<>();
	private HashMap<String, KernelandProcess> processNames = new HashMap<>();
	private KernelandProcess currentProcess;
	private Interrupt interrupt;
	private Timer timer;
	private Clock clock;

	public Scheduler() {
		this.interrupt = new Interrupt(this);
		this.timer = new Timer();
		this.timer.schedule(interrupt, 250, 250);
		this.clock = Clock.tickMillis(ZoneId.systemDefault());
		realTimeProcessList = Collections.synchronizedList(realTimeProcessList);
		interactiveProcessList = Collections.synchronizedList(interactiveProcessList);
		backgroundProcessList = Collections.synchronizedList(backgroundProcessList);
		sleepingProcessList = Collections.synchronizedList(sleepingProcessList);
	}

	/*
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no
	 * process is running
	 * if no, call switchProcess()
	 * 
	 * @Param UserlandProcess up
	 * 
	 * @Return int newProcessPid
	 */
	public int createProcess(UserlandProcess up) {

		KernelandProcess newProcess = new KernelandProcess(up);
		processPids.put(newProcess.getThreadPid(), newProcess);
		processNames.put(newProcess.getProcessName(), newProcess);
		appendToList(newProcess);

		// no running processes
		if (!newProcess.isHasStarted()) {
			switchProcess();
			return newProcess.getThreadPid();
		}
		return newProcess.getThreadPid();
	}

	/*
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no
	 * process is running
	 * if no, call switchProcess()
	 * 
	 * @Param1 UserlandProcess up
	 * 
	 * @Param2 Priority priority
	 * 
	 * @Return int newProcessPid
	 */
	public int createProcess(UserlandProcess up, Priority priority) {

		KernelandProcess newProcess = new KernelandProcess(up, priority);
		processPids.put(newProcess.getThreadPid(), newProcess);
		processNames.put(newProcess.getProcessName(), newProcess);
		appendToList(newProcess);

		// no running processes
		if (!newProcess.isHasStarted()) {
			switchProcess();
			return newProcess.getThreadPid();
		}
		return newProcess.getThreadPid();
	}

	/*
	 * Check if process is running. If yes, stop the process and add to process to
	 * end of list.
	 * Then set current process to first process in the process list, run the
	 * process.
	 */
	public void switchProcess() {

		/*
		 * When a process is stopped, it has to be placed in the correct list
		 * Track condition for process demotion
		 * Use random to pick which list the next process should be selected from
		 */

		wakeUp();

		// while(waitingForMessage.)

		if (currentProcess != null) {

			demotion();

			// Have you started? and are you finished with your task
			// Can add conndition to see if process is running
			if (!currentProcess.isDone() && currentProcess.isHasStarted()) {

				// Stopping currentProcess
				var temp = currentProcess;
				currentProcess = null;

				System.out.println("Process(" + temp.getThreadPid() + "): Stopping");
				temp.stop();

				appendToList(temp);

			}

			//Process is Done
			processPids.remove(currentProcess.getThreadPid());
			processNames.remove(currentProcess.getProcessName());
			// int virtualAddress = UserlandProcess.getTLB()[0][1];
			OS.freeMemory(0,102400);
			
			

			currentProcess = selectProcess();

			System.out.println("Running Process: ID(" + currentProcess.getThreadPid() + ") ("
					+ currentProcess.getPriority() + ")");
			currentProcess.run();

		} else {
			// Either program state is in start up or no process is currently running
			currentProcess = selectProcess();

			if (currentProcess != null) {
				System.out.println("Running Process: ID(" + currentProcess.getThreadPid() + ") ("
						+ currentProcess.getPriority() + ")");
				currentProcess.run();

			} else {
				System.out.println("No available process to select");
			}
		}
	}

	public void sleep(int milliseconds) {
		/*
		 * Need current time
		 * Requested amount of time to sleep, ^milliseconds
		 * Need minimum time to wake up, can only be awaken after this time not before
		 * 
		 * put the sleeping process in the sleepingProcessList
		 * switchProcess() when sleep is called
		 * code to stop a process (putting to sleep)
		 * var tmp = currentlyRunning;
		 * currentlyRunning = null;
		 * tmp.stop( *
		 * wake up process that was sleeping, from one of the list
		 */

		long currentTime = getTime();
		long wakeTime = currentTime + milliseconds;

		currentProcess.setWakeTime(wakeTime);
		System.out.printf("Putting Process: (%d) to sleep for %dms \n", currentProcess.getThreadPid(), milliseconds);
		currentProcess.setTimeOuts(0);
		this.sleepingProcessList.add(this.currentProcess);
		var temp = currentProcess;
		currentProcess = null;
		temp.stop();

		switchProcess();
	}

	public KernelandProcess getCurrentlyRunning() {
		return this.currentProcess;
	}

	public long getTime() {
		return clock.millis();
	}

	private void demote(KernelandProcess currentProcess) {
		switch (currentProcess.getPriority()) {
			case REALTIME:

				System.out.print("Process ID (" + currentProcess.getThreadPid() + ") Demoted from: "
						+ currentProcess.getPriority());
				currentProcess.setPriority(Priority.INTERACTIVE);
				System.out.println(" to " + currentProcess.getPriority());
				break;
			case INTERACTIVE:
				System.out.print("Process ID (" + currentProcess.getThreadPid() + ") Demoted from: "
						+ currentProcess.getPriority());
				currentProcess.setPriority(Priority.BACKGROUND);
				System.out.println(" to " + currentProcess.getPriority());
				break;
			default:
				break;
		}
	}

	private void demotion() {
		if (currentProcess.getTimeOuts() < 5) {

			if (currentProcess.getPriority() != Priority.BACKGROUND) {
				currentProcess.incrementTimeOut();
			}
			System.out.println(
					"Process: ID(" + currentProcess.getThreadPid() + ") Timeouts: " + currentProcess.getTimeOuts());

		} else {

			if (currentProcess.getPriority() != Priority.BACKGROUND) {

				System.out.println("Process: ID(" + currentProcess.getThreadPid() + ") Timed Out: "
						+ currentProcess.getTimeOuts());
				demote(currentProcess);
				currentProcess.setTimeOuts(0);
			}
		}
	}

	private KernelandProcess selectProcess() {
		/*
		 * if there are realTime processes
		 * select realTime process 6/10
		 * if there is interactive processes
		 * select interactive process 3/10
		 * else
		 * select background process 1/10
		 * else if there are interactive processes
		 * select interactive process 3/4
		 * select background process 1/4
		 * else there is only background processes
		 * use first background process
		 */
		Random rand = new Random();

		int[][] TLB = new int[2][2];
		for (int i = 0; i < TLB.length; i++) {
			for (int j = 0; j < TLB.length; j++) {
				TLB[i][j] = -1;
			}
		}
		UserlandProcess.setTLB(TLB);
		UserlandProcess.printTLB();

		if (!this.realTimeProcessList.isEmpty()) {
			// generate random number between 0 and 9
			int select_1 = rand.nextInt(10);

			if (select_1 <= 5) {
				System.out.println("Selecting process from RealTime Processes\n");
				return this.realTimeProcessList.remove(0);

			} else if (select_1 <= 8) {

				if (!interactiveProcessList.isEmpty()) {
					System.out.println("Selecting process from Interactive Processes\n");
					return this.interactiveProcessList.remove(0);

				} else if (!backgroundProcessList.isEmpty()) {
					System.out.println("Selecting process from Background Processes\n");
					return backgroundProcessList.remove(0);

				} else
					System.out.println("Selecting process from RealTime Processes\n");
				return this.realTimeProcessList.remove(0);

			} else {

				if (!backgroundProcessList.isEmpty()) {
					System.out.println("Selecting process from Background Processes\n");
					return this.backgroundProcessList.remove(0);

				} else
					System.out.println("Selecting process from RealTime Processes\n");
				return realTimeProcessList.remove(0);

			}

		} else if (!this.interactiveProcessList.isEmpty()) {

			// generate random number between 0 to 3
			int select_2 = rand.nextInt(4);

			if (select_2 <= 2) {
				System.out.print("Selecting process from Interactive Processes\n");
				return this.interactiveProcessList.remove(0);

			} else if (!backgroundProcessList.isEmpty()) {
				System.out.print("Selecting process from Background Processes\n");
				return this.backgroundProcessList.remove(0);

			} else
				System.out.print("Selecting process from Interactive Processes\n");
			return this.interactiveProcessList.remove(0);

		} else if (!backgroundProcessList.isEmpty()) {
			System.out.print("Selecting process from Background Processes\n");
			return this.backgroundProcessList.remove(0);

		} else if (!sleepingProcessList.isEmpty()) {
			System.out.println("Sleeping List is not empty: " + sleepingProcessList.size());
			System.out.println("Null Process Returned\n");

			return null;
		} else if (!waitingForMessage.isEmpty()) {

			System.out.println("WaitingForMessage hash is not empty: " + waitingForMessage.size());
			System.out.println("Null Process Returned\n");
			return null;

		} else {
			System.exit(0);
		}

		return null;
	}

	/*
	 * Iterate through sleeping list and wake processes ready to be awaken. Then
	 * check if list is empty or not.
	 * Return True if there are no sleeping processes.
	 */
	private void wakeUp() {
		/*
		 * Look through sleeping list and see if any sleeping processes are ready to be
		 * awaken
		 * then put them back into respective list
		 */

		for (int i = 0; i < this.sleepingProcessList.size(); i++) {
			if (sleepingProcessList.get(i).getWakeTime() < getTime()) {

				KernelandProcess awakenProcess = sleepingProcessList.get(i);
				long diff = getTime() - awakenProcess.getWakeTime();

				System.out.print("Waking Process: (" + awakenProcess.getThreadPid() + ")\n");
				System.out.println("Time elapsed from awaken period: " + diff + "ms");

				appendToList(sleepingProcessList.remove(i));
			}
		}
	}

	private void appendToList(KernelandProcess process) {

		switch (process.getPriority()) {
			case REALTIME:
				realTimeProcessList.add(process);
				System.out.printf("Adding Process: ID(%d) to RealTime Processes\n",
						process.getThreadPid());
				break;
			case INTERACTIVE:
				interactiveProcessList.add(process);
				System.out.printf("Adding Process: ID(%d) to Interactive Processes\n",
						process.getThreadPid());
				break;
			case BACKGROUND:
				backgroundProcessList.add(process);
				System.out.printf("Adding Process: ID(%d) to Background Processes\n",
						process.getThreadPid());
				break;
			default:
				break;
		}
	}

	/*
	 * Returns the current process' pid
	 * 
	 * @Return int pid
	 */
	public int getPid() {
		return this.currentProcess.getThreadPid();
	}

	/*
	 * Find the pid for the specified process
	 * 
	 * @Param: String processName
	 * 
	 * @Return: int pid
	 */
	public int getPidByName(String processName) {
		return this.processNames.get(processName).getThreadPid();
	}

	public void sendMessage(KernelMessage msg) {
		KernelMessage message = new KernelMessage(msg);
		message.setSenderPid(getPid());
		KernelandProcess targetProcess = this.processPids.get(message.getTargetPid());

		// target found
		if (targetProcess != null) {
			targetProcess.appendToMessageQueue(message);
		}

		// check to see if target is waiting for a message
		if (this.waitingForMessage.containsValue(targetProcess)) {
			// restore to proper runnable queue
			waitingForMessage.remove(targetProcess.getThreadPid());
			appendToList(targetProcess);
		}
	}

	/*
	 * Dequeues the given process from it's respective queue
	 * 
	 * @Param1: KernelandProcess
	 */
	public void removeFromRunnable(KernelandProcess process) {
		switch (process.getPriority()) {
			case REALTIME:
				realTimeProcessList.remove(process);
				System.out.printf("Removing Process: ID(%d) from RealTime Processes\n",
						process.getThreadPid());
				break;
			case INTERACTIVE:
				interactiveProcessList.remove(process);
				System.out.printf("Removing Process: ID(%d) from Interactive Processes\n",
						process.getThreadPid());
				break;
			case BACKGROUND:
				backgroundProcessList.remove(process);
				System.out.printf("Removing Process: ID(%d) from Background Processes\n",
						process.getThreadPid());
				break;
			default:
				break;
		}
	}

	/*
	 * Checks if process is waiting for a message. If it has received a message it
	 * returns the message.
	 * If the message is not received then
	 */
	public KernelMessage waitForMessage() {

		LinkedList<KernelMessage> currentMessageQueue = getCurrentlyRunning().getMessageQueue();

		if (currentMessageQueue.isEmpty()) {
			removeFromRunnable(getCurrentlyRunning());
			waitingForMessage.put(getCurrentlyRunning().getThreadPid(), getCurrentlyRunning());

			var temp = currentProcess;
			currentProcess = null;
			temp.stop();

			switchProcess();
		}

	//old
		// Check if process has a message
		// if (!currentMessageQueue.isEmpty()) {
		// 	// remove from waiting
		// 	waitingForMessage.remove(getCurrentlyRunning().getThreadPid());
		// 	return currentMessageQueue.removeFirst();
		// }

		// removeFromRunnable(getCurrentlyRunning());
		// waitingForMessage.put(getCurrentlyRunning().getThreadPid(),
		// getCurrentlyRunning());

		// var temp = currentProcess;
		// currentProcess = null;
		// temp.stop();

		// switchProcess();

		waitingForMessage.remove(getCurrentlyRunning().getThreadPid());
		return currentMessageQueue.removeFirst();

		// return null;
	}
}