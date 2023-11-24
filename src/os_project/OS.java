package os_project;

public class OS {

	private static Kernel kernel;
	private static int id;

	/*
	 * Populates kernel with new instance and call createProcess on "init"
	 */
	public static void startUp(UserlandProcess init) {
		VFS vfs = kernel.getVfs();
		OS.kernel = new Kernel();
		OS.kernel.createProcess(init);
		//TODO: Implement create swap file
		/*
		 * PSEUDO
		 * 
		 * Create a file called "swap"
		 * 		use VFS to create file
		 * 		must be created / opened on start up
		 * Need an int to track the page # (next page to write out)?
		 */

		OS.id = vfs.Open("file swap.txt");


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

	public static int getPid() {
		return kernel.getPid();
	}

	public static int getPidByName(String processName) {
		return kernel.getPidByName(processName);
	}

	public static int allocateMemory(int size) {

		if(size % 1024 == 0)
			return kernel.allocateMemory(size);
		
		return -1;
	}

	public static boolean freeMemory(int pointer, int size) {

		if(pointer % 1024 == 0 && size % 1024 == 0)
			return kernel.freeMemory(pointer, size);
		
		return false;
	}

	public static void getMapping(int virtualPageNumber) {
		kernel.getMapping(virtualPageNumber);
		
	}

	public static void sendMessage(KernelMessage msg) {
		kernel.sendMessage(msg);
	}

	public static KernelMessage waitForMessage() {
		return kernel.waitForMessage();
	}

	public static int Open(String s) {
		return kernel.Open(s);
	}

	public static void Close(int id) {
		kernel.Close(id);
	}

	public static byte[] Read(int id, int size) {
		return kernel.Read(id, size);
	}

	public static int Write(int id, byte[] data) {
		return kernel.Write(id, data);
	}

	public static void Seek(int id, int to) {
		kernel.Seek(id, to);
	}

	public static int getId(){
		return OS.id;
	}
}