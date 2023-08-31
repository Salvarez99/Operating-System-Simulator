package os_project;

public class KernelandProcess {

	private static int nextpid = 0;
	private static int pid;
	private boolean hasStarted;
	private Thread thread;
	

	public KernelandProcess(UserlandProcess up) {
		pid = nextpid;
		nextpid = pid++;
		hasStarted = false;
		thread = new Thread(up);
	}
	
	/*
	 * Retrieves pid
	 * @Return int pid
	 */
	public int getThreadPid() {
		return pid;
	}
	
	/*
	 * Check if current process is alive. If yes, suspends the current process
	 */
	public void stop() {
		if (thread.isAlive()) {
			thread.suspend();
		}
	}
	
	/*
	 * Checks if the current process hasStart and isAlive. If yes, returns true otherwise returns false
	 * @Return boolean
	 */
	public boolean isDone() {
		if (hasStarted && !thread.isAlive()) {
			return true;
		}
		return false;
	}
	
	
	/*
	 * Retrieves hasStarted
	 * @Return boolean hasStarted
	 */
	public boolean isHasStarted() {
		return hasStarted;
	}

	/*
	 * resume or start, update started
	 * Check if the current thread has not started. If true, start the thread otherwise check if the 
	 * current thread is waiting. If true, resume the thread
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
