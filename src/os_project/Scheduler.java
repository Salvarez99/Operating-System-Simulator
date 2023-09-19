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
	private LinkedList<KernelandProcess> kernelandProcessList = new LinkedList<>();
	
	private LinkedList<KernelandProcess> RealTimeProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> InteractiveProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> BackgroundProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> SleepingProcessList = new LinkedList<>();
	
	private KernelandProcess currentProcess;
	private Timer timer;
	private Priority priority;
	private Clock clock;
	
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
	
	private Interrupt interrupt;
	
	
	public Scheduler() {
//		this.priority = Priority.INTERACTIVE;
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
		
		OS.setPriority(Priority.INTERACTIVE);
		currentProcess = new KernelandProcess(up);
		kernelandProcessList.add(currentProcess);
		
		if (!currentProcess.isHasStarted()) { //no running processes
			switchProcess();
			return currentProcess.getThreadPid();
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
//		this.priority = priority;
		OS.setPriority(priority);
		currentProcess = new KernelandProcess(up);
		kernelandProcessList.add(currentProcess);
		
		if (!currentProcess.isHasStarted()) { //no running processes
			switchProcess();
			return currentProcess.getThreadPid();
		}
		
		return currentProcess.getThreadPid();
	}
	
	
	/*
	 * Check if process is running. If yes, stop the process and add to process to end of list.
	 * Then set current process to first process in the process list, run the process.  
	 */
	public void switchProcess() {
		
		//when a process is stopped, it has to be placed in the correct list
		//track condition for process demotion
		//use random to pick which list the next process should be selected from
		
		if (!currentProcess.isDone() && currentProcess.isHasStarted()) {
			
			currentProcess.stop();
			if (!currentProcess.isDone()) {
				kernelandProcessList.add(currentProcess);
			}
		}
		currentProcess = kernelandProcessList.remove();
		currentProcess.run();
	}
	
	public void sleep(int milliseconds) {
		//Need current time
		//Requested amount of time to sleep, ^milliseconds
		//Need minimum time to wake up, can only be awaken after this time not before it
		
		//switchProcess() when sleep is called
		//put the sleeping process in the sleepingProcessList
		//code to stop a process (putting to sleep)
			//var tmp = currentlyRunning;
			//currentlyRunning = null;
			//tmp.stop();

		//wake up process that was sleeping, from one of the list
		
		long currentTime = getTime();
		long alarm = currentTime + milliseconds;
		
		//switchProcess()
		var tmp = currentProcess;
		currentProcess = null;
		tmp.stop();
		this.SleepingProcessList.add(tmp);
		
		
	}
	
	public long getTime() {
		return clock.millis();
	}
	
	public void demote() {
		switch (OS.getPriority()){
			case REALTIME:
				OS.setPriority(Priority.INTERACTIVE);
				break;			
			case INTERACTIVE:
				OS.setPriority(Priority.BACKGROUND);
				break;
			default:
				break;
		}
			
	}
	
	
	
	
}
