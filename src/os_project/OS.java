package os_project;

public class OS { //supposed to be static

	private static Kernel kernel;
	
	/*
	 * Populates kernel with new instance and call createProcess on "init"
	 */
	public static void startUp(UserlandProcess init) {
		
		kernel = new Kernel();
		kernel.createProcess(init);
	}
	
	public static int createProcess(UserlandProcess up) {
		return kernel.createProcess(up);
	}
	
	
}
