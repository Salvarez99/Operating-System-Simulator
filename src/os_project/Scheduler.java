package os_project;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	private LinkedList<KernelandProcess> kernelandProcessList = new LinkedList<>();
	private Timer timer;
	private KernelandProcess kernelandProcess;
//	private Scheduler scheduler;
	
	private class Interrupt extends TimerTask{

		//scheduler init here
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
	
	
	//edited
	public Scheduler() {
		//schedule interrupt using timer class
		//Don't think this is right, what is the TimerTask parameter supposed to be?
		
		// interrupt init
		interrupt = new Interrupt(this);
		timer = new Timer();
		timer.schedule(interrupt, 250, 250);
	}
	
	//edited
	public int createProcess(UserlandProcess up) {
		/*
		 * Construct a kernelandProcess
		 * Add it to the end of the list
		 * if no process is running 
		 * 	Call switchProcess()
		 * 	return pid of new process
		 * 
		 */
		
		//How do we initialize the new process?
		kernelandProcess = new KernelandProcess(up);
		kernelandProcessList.add(kernelandProcess);
		
		if (!kernelandProcess.isHasStarted()) { //no running processes
			switchProcess();
			return kernelandProcess.getThreadPid();
		}
		
		return kernelandProcess.getThreadPid();
	}
	
	//edited
	public void switchProcess() {
		
		/*
		 * If the process is running (how do we check if the process is running?)
		 * 	Stop the process
		 * 	If process is not done
		 * 		Add process to the end of the list
		 * 
		 * Set current process equal to 0 index of process list
		 * call run on the process
		 */
		
		if (!kernelandProcess.isDone() && kernelandProcess.isHasStarted()) {
			
			kernelandProcess.stop();
			if (!kernelandProcess.isDone()) {
				kernelandProcessList.add(kernelandProcess);
			}
			
		}
		kernelandProcess = kernelandProcessList.remove();
		kernelandProcess.run();
		
	}
	
	
	
}
