package os_project;

public class OS { //supposed to be static

	private static Kernel kernel;
	
	//unsure
	public static void startUp(UserlandProcess init) {
		
		kernel = new Kernel();
		//populate kernel with new instance and call createProcess on "init"
		kernel.createProcess(init);
	}
	
	//unsure
	public static int createProcess(UserlandProcess up) {
		return kernel.createProcess(up);
	}
	
	
}
