package os_project;

public class Kernel implements Device {
	private static Scheduler scheduler = new Scheduler();
	private VFS vfs = new VFS();

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
		KernelandProcess currentProcess = scheduler.getCurrentlyRunning();
		int[] cpDeviceIds = currentProcess.getDeviceIds();
		int vfsId;

		// Check for open index, otherwise return failure
		for (int i = 0; i < cpDeviceIds.length; i++) {
			if (cpDeviceIds[i] == -1) {
				vfsId = this.vfs.Open(s);
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
		KernelandProcess currentProcess = scheduler.getCurrentlyRunning();
		int[] cpDeviceIds = currentProcess.getDeviceIds();
		int vfsId = cpDeviceIds[id];
		byte[] bArray = this.vfs.Read(vfsId, size);
		return bArray;
	}

	@Override
	public int Write(int id, byte[] data) {

		if (data.length > 0) {
			KernelandProcess currentProcess = scheduler.getCurrentlyRunning();
			int[] cpDeviceIds = currentProcess.getDeviceIds();
			int vfsId = cpDeviceIds[id];
			this.vfs.Write(vfsId, data);
			return data.length;
		}

		return 0;
	}

	@Override
	public void Seek(int id, int to) {
		KernelandProcess currentProcess = scheduler.getCurrentlyRunning();
		int[] cpDeviceIds = currentProcess.getDeviceIds();

		int vfsId = cpDeviceIds[id];
		this.vfs.Seek(vfsId, to);
	}
}
