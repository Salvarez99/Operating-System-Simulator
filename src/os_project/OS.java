package os_project;

public class OS { //supposed to be static

	private static Kernel kernel;
	
	//unsure
	public static void Startup(UserlandProcess init) {
		//populate kernel with new instance and call createProcess on "init"
	}
	
	//unsure
	public static int CreateProcess(UserlandProcess up) {
		return kernel.createProcess(up);
	}
	
	
}
