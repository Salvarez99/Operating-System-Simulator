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
	private boolean running;

	public KernelandProcess(UserlandProcess up) {
		this.pid = KernelandProcess.nextpid - 1;
		KernelandProcess.nextpid++;
		this.hasStarted = false;
		this.running = false;
		this.thread = new Thread(up);
		this.priority = Priority.INTERACTIVE;
		this.timeOuts = 0;
	}

	public KernelandProcess(UserlandProcess up, Priority priority) {
		this.pid = KernelandProcess.nextpid - 1;
		KernelandProcess.nextpid++;
		this.hasStarted = false;
		this.running = false;
		this.thread = new Thread(up);
		this.priority = priority;
		this.timeOuts = 0;
	}

	/*
	 * Retrieves pid
	 * 
	 * @Return int pid
	 */
	public int getThreadPid() {
		return pid;
	}

	/*
	 * Check if current process is alive. If yes, suspends the current process
	 */
	//TODO: Check stop() logic in debugging
	@SuppressWarnings("removal")
	public void stop() {
		if (thread.isAlive() && isRunning()) {
			// System.out.println("Stopping thread: ID(" + thread.getId() + ")");
			// hasStarted = false;
			running = false;
			try {
				System.out.println("Thread: ID(" + thread.getId() + ") has been suspended");
				thread.suspend();
			} catch (Exception e) {

				System.out.println("Exception while suspending thread: " + e.getMessage());
			}

		} else {
			System.out.println("Thread: ID(" + thread.getId() + ") was not stopped");
			System.out.println("Thread: ID(" + thread.getId() + ") state: " + thread.getState());
			System.out.println("Thread: ID(" + thread.getId() + ") isAlive(): " + thread.isAlive());
		}

	}

	/*
	 * Checks if the current process hasStart and isAlive. If yes, returns true
	 * otherwise returns false
	 * 
	 * @Return boolean
	 */
	//TODO: Check isDone() logic in debugging
	public boolean isDone() {
		if (hasStarted && !thread.isAlive()) {
			return true;
		}
		return false;
	}

	public boolean isRunning() {
		if (this.running == true) {
			return true;
		}
		return false;
	}

	/*
	 * Retrieves hasStarted
	 * 
	 * @Return boolean hasStarted
	 */
	public boolean isHasStarted() {
		return hasStarted;
	}

	/*
	 * resume or start, update started
	 * Check if the current thread has not started. If true, start the thread
	 * otherwise check if the
	 * current thread is waiting. If true, resume the thread
	 */
	@SuppressWarnings("removal")
	//TODO: Check logic for run() in debugging
	public void run() {
		Thread.State prevState = thread.getState();
		System.out.println("Previous thread Status: " + prevState);

		if (!isHasStarted() /* && thread.getState() == State.NEW */) {
			System.out.println("Starting a new thread: ID(" + thread.getId() + ")");

			hasStarted = true;
			running = true;
			thread.start();

		} else if (thread.isAlive() /*
									 * thread.getState() == Thread.State.WAITING || thread.getState() ==
									 * Thread.State.RUNNABLE
									 */) {

			System.out.println("Running thread: ID(" + thread.getId() + ")");
			running = true;
			thread.resume();
		}

		Thread.State currState = thread.getState();
		if (currState != prevState) {
			System.out.println("Thread State Changed: " + prevState + " -> " + currState);
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

	public void incrementTimeOut() {
		this.timeOuts++;
	}

	public Thread getThread() {
		return this.thread;
	}
}
