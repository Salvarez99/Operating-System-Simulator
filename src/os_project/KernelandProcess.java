package os_project;

import java.lang.Thread.State;

public class KernelandProcess {

	private static int nextpid = 2;
	private int pid;
	private boolean hasStarted;
	private Thread thread;
	private Priority priority;
	private long wakeTime;
	private int timeOuts;

	public KernelandProcess(UserlandProcess up) {
		this.pid = KernelandProcess.nextpid - 1;
		KernelandProcess.nextpid++;
		this.hasStarted = false;
		this.thread = new Thread(up);
		this.priority = Priority.INTERACTIVE;
		this.timeOuts = 0;
	}
	
	public KernelandProcess(UserlandProcess up, Priority priority) {
		this.pid = KernelandProcess.nextpid - 1;
		KernelandProcess.nextpid++;
		this.hasStarted = false;
		this.thread = new Thread(up);
		this.priority = priority;
		this.timeOuts = 0;
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
	@SuppressWarnings("removal")
	public void stop() {
		if (thread.isAlive()) {
			try {
				thread.suspend();
			}catch(Exception e) {};
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
	@SuppressWarnings("removal")
	public void run() {
		System.out.println("Thread Status: " + thread.getState());
		if (!isHasStarted() && thread.getState() == State.NEW) {
			thread.start();
			hasStarted = true;
			
		}else if(thread.getState() == Thread.State.WAITING || thread.getState() == Thread.State.RUNNABLE) {

//			try {
				thread.resume();
//			}catch(Exception e) {};
		}
	}
	
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public long getWakeTime() {
		return wakeTime;
	}

	public void setWakeTime(long wakeTime) {
		this.wakeTime = wakeTime;
	}

	public int getTimeOuts() {
		return timeOuts;
	}

	public void setTimeOuts(int timeOuts) {
		this.timeOuts = timeOuts;
	}
	
	public void incrementTimeOut(){
		this.timeOuts++;
	}
}
