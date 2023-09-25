package os_project;
import java.util.List;
import java.util.LinkedList;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Collections;


public class Scheduler {

	private class Interrupt extends TimerTask{
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
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no process is running
	 * if no, call switchProcess()
	 * @Param UserlandProcess up
	 * @Return int newProcessPid
	 */
	public synchronized int createProcess(UserlandProcess up) {

		KernelandProcess newProcess = new KernelandProcess(up);
		appendToList(newProcess);

		//no running processes
		if (!newProcess.isHasStarted()) { 
			switchProcess();
			return newProcess.getThreadPid();
		}
		return newProcess.getThreadPid();
	}

	/*
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no process is running
	 * if no, call switchProcess()
	 * @Param1 UserlandProcess up
	 * @Param2 Priority priority
	 * @Return int newProcessPid
	 */
	public synchronized int createProcess(UserlandProcess up, Priority priority) {

		KernelandProcess newProcess = new KernelandProcess(up, priority);
		appendToList(newProcess);

		//no running processes
		if (!newProcess.isHasStarted()) { 
			switchProcess();
			return newProcess.getThreadPid();
		}
		return newProcess.getThreadPid();
	}

	/*
	 * Check if process is running. If yes, stop the process and add to process to end of list.
	 * Then set current process to first process in the process list, run the process.  
	 */
	public synchronized void switchProcess() {

		//when a process is stopped, it has to be placed in the correct list
		//track condition for process demotion
		//use random to pick which list the next process should be selected from


		while(!sleepingProcessList.isEmpty()){
			wakeUp();
		}



		if(currentProcess != null){

			wakeUp();

			currentProcess.incrementTimeOut();

			if(currentProcess.getTimeOuts()  == 5){

				if(currentProcess.getPriority() != Priority.BACKGROUND) {

					demote(currentProcess);
					currentProcess.setTimeOuts(0);
				}
			}

			if (!currentProcess.isDone() && currentProcess.isHasStarted()) {

				System.out.println("Process(" + currentProcess.getThreadPid() + "): Stopping\n");

				//Stopping currentProcess
				var temp = currentProcess;
				currentProcess = null;
				temp.stop();

				if (!temp.isDone()) {
					appendToList(temp);
				}
			}

			currentProcess = selectProcess();

			if(currentProcess != null) {
				System.out.println("Running Process: ID(" + currentProcess.getThreadPid() + ") (" + currentProcess.getPriority() +")");
				currentProcess.run();
			}else {

			}


		}else {
			currentProcess = selectProcess();
			if(currentProcess != null) {
				System.out.println("Running Process: ID(" + currentProcess.getThreadPid() + ") (" + currentProcess.getPriority() +")");
				currentProcess.run();
			}else {

			}
		}
	}

	public synchronized void sleep(int milliseconds) {
		//Need current time
		//Requested amount of time to sleep, ^milliseconds
		//Need minimum time to wake up, can only be awaken after this time not before it

		//put the sleeping process in the sleepingProcessList
		//switchProcess() when sleep is called
		//code to stop a process (putting to sleep)
		//var tmp = currentlyRunning;
		//currentlyRunning = null;
		//tmp.stop();

		//wake up process that was sleeping, from one of the list

		long currentTime = getTime();
		long wakeTime = currentTime + milliseconds;
		currentProcess.setWakeTime(wakeTime);

		System.out.println("Putting Process: (" + currentProcess.getThreadPid() + ") to sleep");
		currentProcess.setTimeOuts(0);
		this.sleepingProcessList.add(this.currentProcess);
		switchProcess();
	}

	public long getTime() {
		return clock.millis();
	}

	private void demote(KernelandProcess currentProcess) {
		switch (currentProcess.getPriority()){
		case REALTIME:

			System.out.print("Process ID (" + currentProcess.getThreadPid() +") Demoted from: " + currentProcess.getPriority());
			currentProcess.setPriority(Priority.INTERACTIVE);
			System.out.println(" to " + currentProcess.getPriority());
			break;			
		case INTERACTIVE:
			System.out.print("Process ID (" + currentProcess.getThreadPid() +") Demoted from: " + currentProcess.getPriority());
			currentProcess.setPriority(Priority.BACKGROUND);
			System.out.println(" to " + currentProcess.getPriority());
			break;
		default:
			break;
		}
	}

	private synchronized KernelandProcess selectProcess() {
		/*
		 * if there are realTime processes
		 * 	  select realTime process 6/10
		 *    if there is interactive processes
		 * 		  select interactive process 3/10
		 * 	  else
		 * 		  select background process 1/10 
		 * else if there are interactive processes
		 * 	  select interactive process 3/4
		 * 	  select background process 1/4
		 * else there is only background processes 
		 * 	  use first background process
		 */
		Random rand = new Random();


		if(!this.realTimeProcessList.isEmpty()) {
			//generate random number between 0 and 9
			int select_1 = rand.nextInt(10);

			if(select_1 <= 5) {
				return this.realTimeProcessList.remove(0);

			}else if(select_1 <= 8) {

				if(!interactiveProcessList.isEmpty()){
					return this.interactiveProcessList.remove(0);
				}else if(!backgroundProcessList.isEmpty()){
					return backgroundProcessList.remove(0);
				}else
					return this.realTimeProcessList.remove(0);

			}else{

				if(!backgroundProcessList.isEmpty()){
					return this.backgroundProcessList.remove(0);
				}else 
					return realTimeProcessList.remove(0);
			}

		}else if(!this.interactiveProcessList.isEmpty()) {

			//generate random number between 0 to 3
			int select_2 = rand.nextInt(4);

			if(select_2 <= 2){
				return this.interactiveProcessList.remove(0);
			}else if(!backgroundProcessList.isEmpty()) {
				return this.backgroundProcessList.remove(0);
			}else
				return this.interactiveProcessList.remove(0);

		}else if(!backgroundProcessList.isEmpty()){
			return this.backgroundProcessList.remove(0);
			
		}else if(!sleepingProcessList.isEmpty()) {
			return null;
		}else {
			System.exit(0);
		}

		return null;
	}

	/*
	 * Iterate through sleeping list and wake processes ready to be awaken. Then check if list is empty or not.
	 * Return True if there are no sleeping processes.
	 */
	private synchronized void wakeUp(){
		/*
		 * Look through sleeping list and see if any sleeping processes are ready to be awaken
		 * then put them back into respective list
		 */

		for(int i = 0; i < this.sleepingProcessList.size(); i++){
			if(sleepingProcessList.get(i).getWakeTime() < getTime()){

				KernelandProcess awakenProcess = sleepingProcessList.get(i);
				System.out.println("Waking Process: (" + awakenProcess.getThreadPid() + ")");
				appendToList(sleepingProcessList.remove(i));
			}
		}
	}

	private synchronized void appendToList(KernelandProcess process) {
		switch(process.getPriority()) {
		case REALTIME:
			realTimeProcessList.add(process);
			break;
		case INTERACTIVE:
			interactiveProcessList.add(process);
			break;
		case BACKGROUND:
			backgroundProcessList.add(process);
			break;
		default:
			break;
		}
	}
}