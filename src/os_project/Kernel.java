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

	// TODO: Decipher this
	@Override
	public int Open(String s) {
		KernelandProcess currentProcess = Scheduler.getCurrentlyRunning();
		int[] deviceIds = currentProcess.getDeviceIds();
		VFS vfs = new VFS();
		int id;

		for (int i = 0; i < deviceIds.length; i++) {
			if (deviceIds[i] == -1) {
				id = vfs.Open(s);

				if (id == -1) {
					return -1;
				} else {
					deviceIds[i] = id;

					return id;
				}
			} else {
				return -1;
				// call here
			}
		}

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
