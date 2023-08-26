package os_project;

public class Kernel {
	private Scheduler scheduler;
	
	//unsure
	public int createProcess(UserlandProcess up) {
		return scheduler.createProcess(up);
	}
}
