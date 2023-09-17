package os_project;
import os_project.Priority;

public class Kernel {
	private Scheduler scheduler = new Scheduler();
	Priority priority;
	
	public Kernel() {
		this.priority = Priority.INTERACTIVE;
	}
	
	public int createProcess(UserlandProcess up) {
		return scheduler.createProcess(up);
	}
	
	public int createProcess(UserlandProcess up, Priority priority) {
		this.priority = priority;
		return scheduler.createProcess(up);
	}
	
	public void sleep(int milliseconds) {
		scheduler.sleep(milliseconds);
	}
}
