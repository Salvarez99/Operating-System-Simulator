package os_project;
import os_project.Priority;

public class OS { //supposed to be static

	private static Kernel kernel;
	private static Priority priority;
	
	/*
	 * Populates kernel with new instance and call createProcess on "init"
	 */
	public static void startUp(UserlandProcess init) {
		
		OS.kernel = new Kernel();
		OS.kernel.createProcess(init);
		OS.priority = Priority.INTERACTIVE;
	}
	
	public static int createProcess(UserlandProcess up) {
		return kernel.createProcess(up);
		
	}
	
	public static int createProcess(UserlandProcess up, Priority priority) {
		OS.priority = priority;
		return kernel.createProcess(up);
		
	}

	public static void sleep(int milliseconds) {
		
	}
	
}
