package os_project;

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
	 * resume or start, update started
	 * Check if the current thread has not started. If true, start the thread
	 * otherwise check if the
	 * current thread is waiting. If true, resume the thread
	 */
	@SuppressWarnings("removal")
	public void run() {
		Thread.State prevState = thread.getState();
		Thread.State currState = null;


		if (!isHasStarted()) {
			
			System.out.println("*****Starting a new thread: ID(" + thread.getId() + ")");
			hasStarted = true;
			running = true;
			thread.start();

			currState = thread.getState();

		} else if (thread.isAlive() && !isRunning()) {

			running = true;
			System.out.println("*****Running thread: ID(" + thread.getId() + ")");
			thread.resume();
			currState = thread.getState();


		}

		if (currState != prevState) {
			currState = thread.getState();
			System.out.println("*****Thread State("+ thread.getId() +") Changed: " + prevState + " -> " + currState);
		}
		
	}

	/*
	 * Check if current process is alive. If yes, suspends the current process
	 */
	@SuppressWarnings("removal")
	public void stop() {
		if (thread.isAlive() && isRunning()) {
			running = false;
			try {
				System.out.println("*****Thread: ID(" + thread.getId() + ") has been suspended\n\n");
				thread.suspend();
			} catch (Exception e) {
				System.out.println("*****Exception while suspending thread: " + e.getMessage());
			}

		}
	}

	/*
	 * Checks if the current process hasStart and isAlive. If yes, returns true
	 * otherwise returns false
	 * 
	 * @Return boolean
	 */
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
	 * Retrieves pid
	 * 
	 * @Return int pid
	 */
	public int getThreadPid() {
		return pid;
	}

	/*
	 * Retrieves hasStarted
	 * 
	 * @Return boolean hasStarted
	 */
	public boolean isHasStarted() {
		return hasStarted;
	}

	public void setRunning(boolean running) {
		this.running = running;
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
