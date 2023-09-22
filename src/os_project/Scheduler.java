package os_project;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import os_project.Priority;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Random;
import java.util.Collections;


public class Scheduler {
//	private LinkedList<KernelandProcess> kernelandProcessList = new LinkedList<>();
	
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
	
	private LinkedList<KernelandProcess> realTimeProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> interactiveProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> backgroundProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> sleepingProcessList = new LinkedList<>();
	private KernelandProcess currentProcess;
	private Interrupt interrupt;
	private Timer timer;
	private Clock clock;
	
	public Scheduler() {
		this.interrupt = new Interrupt(this);
		this.timer = new Timer();
		this.timer.schedule(interrupt, 250, 250);
		this.clock = Clock.tickMillis(ZoneId.systemDefault());
	}
	
	/*
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no process is running
	 * if no, call switchProcess()
	 * @Param UserlandProcess up
	 * @Return int newProcessPid
	 */
	public int createProcess(UserlandProcess up) {
		this.currentProcess = new KernelandProcess(up);
		interactiveProcessList.add(this.currentProcess);
		
		if (!currentProcess.isHasStarted()) { //no running processes
			switchProcess();
			return this.currentProcess.getThreadPid();
		}
		return currentProcess.getThreadPid();
	}
	
	/*
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no process is running
	 * if no, call switchProcess()
	 * @Param1 UserlandProcess up
	 * @Param2 Priority priority
	 * @Return int newProcessPid
	 */
	public int createProcess(UserlandProcess up, Priority priority) {
		this.currentProcess = new KernelandProcess(up, priority);
		appendToList(currentProcess);
//		kernelandProcessList.add(currentProcess);
		
		if (!currentProcess.isHasStarted()) { //no running processes
			switchProcess();
			return this.currentProcess.getThreadPid();
		}
		return this.currentProcess.getThreadPid();
	}
	
	
	/*
	 * Check if process is running. If yes, stop the process and add to process to end of list.
	 * Then set current process to first process in the process list, run the process.  
	 */
	public void switchProcess() {
		
		//when a process is stopped, it has to be placed in the correct list
		//track condition for process demotion
		//use random to pick which list the next process should be selected from
		
		/*
		 * look through sleeping list and see if any sleeping processes are ready to be awaken
		 * then put them back into respective list
		 * 
		 */
		
		/*
		 * add one to consecutive time then check if it has used alloted time 5 times already
		 * if so call demotion
		 *  
		 */
		
		
		if (!currentProcess.isDone() && currentProcess.isHasStarted()) {
			
//			KernelandProcess newProcess = selectProcess();
			
			//Stopping currentProcess
			var tmp = currentProcess;
			currentProcess = null;
			tmp.stop();

			if (!currentProcess.isDone()) {
				appendToList(currentProcess);
//				kernelandProcessList.add(currentProcess);
			}
			
//			currentProcess = newProcess;
		}
		currentProcess = selectProcess();
		currentProcess.run();
	}
	
	public void sleep(int milliseconds) {
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
		
		this.sleepingProcessList.add(this.currentProcess);
		switchProcess();
	}
	
	public long getTime() {
		return clock.millis();
	}
	
	private void demote(KernelandProcess currentProcess) {
		switch (currentProcess.getPriority()){
			case REALTIME:
				currentProcess.setPriority(Priority.INTERACTIVE);
				break;			
			case INTERACTIVE:
				currentProcess.setPriority(Priority.BACKGROUND);
				break;
			default:
				break;
		}
	}
	
	private KernelandProcess selectProcess() {
		Random rand = new Random();
		//generate random number between 0 and 9
		int listNum = rand.nextInt(10);
		
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

		if(!this.realTimeProcessList.isEmpty()) {
			if(listNum <= 5) {
				return this.realTimeProcessList.remove();
			}else if(!this.interactiveProcessList.isEmpty() && listNum <= 8) {
				return this.interactiveProcessList.remove();
			}else 
				return this.backgroundProcessList.remove();
		}else if(!this.interactiveProcessList.isEmpty()) {
			if(listNum <= 6) // 7/10 
				return this.interactiveProcessList.remove();
			return this.backgroundProcessList.remove();
		}
		return this.backgroundProcessList.remove();
	}
	
	private void appendToList(KernelandProcess process) {
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
