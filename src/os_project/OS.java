package os_project;

public class OS {

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
}