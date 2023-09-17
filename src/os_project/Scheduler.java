package os_project;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import os_project.Priority;
import java.time.Clock;
import java.util.Random;
import java.util.Collections;


public class Scheduler {
	private LinkedList<KernelandProcess> kernelandProcessList = new LinkedList<>();
	
	private LinkedList<KernelandProcess> RealTimeProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> InteractiveProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> BackgroundProcessList = new LinkedList<>();
	private LinkedList<KernelandProcess> SleepingProcessList = new LinkedList<>();
	
	private KernelandProcess kernelandProcess;
	private Timer timer;
	Priority priority;
	
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
		this.priority = Priority.INTERACTIVE;
		interrupt = new Interrupt(this);
		timer = new Timer();
		timer.schedule(interrupt, 250, 250);
	}
	
	/*
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no process is running
	 * if no, call switchProcess()
	 * @Param UserlandProcess up
	 * @Return int newProcessPid
	 */
	public int createProcess(UserlandProcess up) {
		
		kernelandProcess = new KernelandProcess(up);
		kernelandProcessList.add(kernelandProcess);
		
		if (!kernelandProcess.isHasStarted()) { //no running processes
			switchProcess();
			return kernelandProcess.getThreadPid();
		}
		
		return kernelandProcess.getThreadPid();
	}
	
	/*
	 * Constructs a new KernelandProcess, adds it to the processList. Checks if no process is running
	 * if no, call switchProcess()
	 * @Param1 UserlandProcess up
	 * @Param2 Priority priority
	 * @Return int newProcessPid
	 */
	public int createProcess(UserlandProcess up, Priority priority) {
		this.priority = priority;
		kernelandProcess = new KernelandProcess(up);
		kernelandProcessList.add(kernelandProcess);
		
		if (!kernelandProcess.isHasStarted()) { //no running processes
			switchProcess();
			return kernelandProcess.getThreadPid();
		}
		
		return kernelandProcess.getThreadPid();
	}
	
	
	/*
	 * Check if process is running. If yes, stop the process and add to process to end of list.
	 * Then set current process to first process in the process list, run the process.  
	 */
	public void switchProcess() {
		
		if (!kernelandProcess.isDone() && kernelandProcess.isHasStarted()) {
			
			kernelandProcess.stop();
			if (!kernelandProcess.isDone()) {
				kernelandProcessList.add(kernelandProcess);
			}
		}
		kernelandProcess = kernelandProcessList.remove();
		kernelandProcess.run();
	}
	
	public void sleep(int milliseconds) {
		
	}
}
