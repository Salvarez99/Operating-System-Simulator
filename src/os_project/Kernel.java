package os_project;

public class Kernel {
	private static Scheduler scheduler = new Scheduler();
	
	public Kernel() {
	}
	
	public int createProcess(UserlandProcess up) {
		return scheduler.createProcess(up);
	}
	
	public int createProcess(UserlandProcess up, Priority priority) {
		return scheduler.createProcess(up, priority);
	}
	
	public static void sleep(int milliseconds) {
		scheduler.sleep(milliseconds);
	}
}
