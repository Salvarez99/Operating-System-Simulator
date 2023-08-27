package os_project;

public class KernelandProcess {

	private static int nextpid = 0;
	private int pid;
	boolean hasStarted;
	private Thread thread;
	
	//When do I start the thread?
	/*
	 * Do I start it in the constructor? or the KernalLandProcess()?
	 */
	public KernelandProcess(UserlandProcess up) {
		pid = nextpid;
		nextpid = pid++;
		hasStarted = false;
		thread = new Thread(up);
	}
	
	
	public int getThreadPid() {
		return pid;
	}
	
	
	public void stop() {
		if (thread.isAlive()) {
			thread.suspend(); //NOT STOP SUSPEND. B I TCH
		}
	}
	
	//unsure
	public boolean isDone() {
		if (hasStarted && !thread.isAlive()) {
			return true;
		}
		return false;
	}
	
	
	
	public boolean isHasStarted() {
		return hasStarted;
	}

	//unsure
	/*
	 * resume or start, update started
	 */
	public void run() {
		if (!isHasStarted()) {
			thread.start();
			hasStarted = true;
		}else if(thread.getState() == Thread.State.WAITING) {
			thread.resume();
		}
		
		
	}
	
}
