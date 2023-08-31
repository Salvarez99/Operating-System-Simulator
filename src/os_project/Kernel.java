package os_project;

public class Kernel {
	private Scheduler scheduler = new Scheduler();
	
	public Kernel() {
		
	}
	
	public int createProcess(UserlandProcess up) {
		return scheduler.createProcess(up);
	}
}
