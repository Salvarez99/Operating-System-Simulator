package os_project;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	private LinkedList<KernelandProcess> kernelandProcessList = new LinkedList<>();
	private Timer timer;
	private KernelandProcess kernelandProcess;
	private Scheduler scheduler;
	
	private class Interrupt extends TimerTask{

		@Override
		public void run() {
			scheduler.switchProcess();
			
		}
		
	}
	
	private Interrupt interrupt;
	
	
	
	public Scheduler() {
		//schedule interrupt using timer class
		//Don't think this is right, what is the TimerTask parameter supposed to be?
		timer.schedule(interrupt, 250, 250);
	}
	
	//unsure
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
		KernelandProcess newKernelProcess = null;
		kernelandProcessList.add(newKernelProcess);
		
		if (true) {
			switchProcess();
			return kernelandProcess.getThreadPid();
		}
		
		return kernelandProcess.getThreadPid();
	}
	
	//unsure
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
		
		if (true) {
			
			kernelandProcess.stop();
			if (!kernelandProcess.isDone()) {
				kernelandProcessList.add(kernelandProcess);
			}
			
			kernelandProcess = kernelandProcessList.getFirst();
			kernelandProcess.run();
		}
		
	}
	
	
	
}
