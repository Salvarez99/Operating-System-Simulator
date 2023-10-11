package os_project;

public class Kernel implements Device {
	private static Scheduler scheduler = new Scheduler();

	public Kernel() {
	}

	public int createProcess(UserlandProcess up) {
		return scheduler.createProcess(up);
	}

	public int createProcess(UserlandProcess up, Priority priority) {
		return scheduler.createProcess(up, priority);
	}

	public static void sleep(int milliseconds) {
		scheduler.sleep(milliseconds);
	}

	@Override
	public int Open(String s) {
		KernelandProcess currentProcess = Scheduler.getCurrentlyRunning();
		int[] cpDeviceIds = currentProcess.getDeviceIds();
		VFS vfs = new VFS();
		int vfsId;

		// Check for open index, otherwise return failure
		for (int i = 0; i < cpDeviceIds.length; i++) {
			if (cpDeviceIds[i] == -1) {
				vfsId = vfs.Open(s);
				cpDeviceIds[i] = vfsId;
				return i;
			}
		}

		return -1;
	}

	@Override
	public void Close(int id) {

	}

	@Override
	public byte[] Read(int id, int size) {
		
	}

	@Override
	public int Write(int id, byte[] data) {

	}

	@Override
	public void Seek(int id, int to) {

	}
}
