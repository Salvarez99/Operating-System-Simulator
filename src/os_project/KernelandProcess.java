package os_project;

public class KernelandProcess {

	private static int nextpid = 0;
	private int pid = 0; //maybe?
	boolean hasStarted;
	Thread thread;
	
	//When do I start the thread?
	/*
	 * Do I start it in the constructor? or the KernalLandProcess()?
	 */
	
	
	public KernelandProcess(){
		pid = 0;
		hasStarted = false;
		thread = new Thread();
		nextpid = pid++;
	}
	
	public void KernelLandProcess(UserlandProcess up) {
		thread = new Thread();
		pid++;
		nextpid++;
		
	}
	
	public void stop() {
		if (thread.isAlive()) {
			thread.stop();
			hasStarted = false;
		}
	}
	
	//unsure
	public boolean isDone() {
		if (hasStarted && !thread.isAlive()) {
			return true;
		}
		return false;
	}
	
	//unsure
	public void run() {
		if (!thread.isAlive()) {
			thread.resume();
			//or?
			thread.start();
		}
	}
	
}
