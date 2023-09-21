package os_project;

public class OS { //supposed to be static

	private static Kernel kernel;
	
	/*
	 * Populates kernel with new instance and call createProcess on "init"
	 */
	public static void startUp(UserlandProcess init) {
		
		OS.kernel = new Kernel();
		OS.kernel.createProcess(init);
	}
	
	public static int createProcess(UserlandProcess up) {
		return kernel.createProcess(up);
		
	}
	
	public static int createProcess(UserlandProcess up, Priority priority) {
		return kernel.createProcess(up, priority);
		
	}

	public static void sleep(int milliseconds) {
		Kernel.sleep(milliseconds);
	}
	
}
