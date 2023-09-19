package os_project;
import os_project.Priority;

public class Kernel {
	private static Scheduler scheduler = new Scheduler();
//	private Priority priority;
	
	public Kernel() {
		OS.setPriority(Priority.INTERACTIVE);
	}
	
	public int createProcess(UserlandProcess up) {
		OS.setPriority(Priority.INTERACTIVE);
		return scheduler.createProcess(up);
	}
	
	public int createProcess(UserlandProcess up, Priority priority) {
//		this.priority = priority;
		OS.setPriority(priority);
		return scheduler.createProcess(up);
	}
	
	public static void sleep(int milliseconds) {
		scheduler.sleep(milliseconds);
	}
}
