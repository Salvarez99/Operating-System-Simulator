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

	/*
	 * Checks if there is an available space for a device to be opened in the
	 * current process then calls vfs.open
	 * and stores the returned id into the current process' list of device ids
	 * 
	 * @Param: String s
	 * 
	 * @Return: index position of current device
	 */
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

	/*
	 * Uses the id to find the matching vfs id and close that device
	 * 
	 * @Param: int id, id of device being closed
	 */
	@Override
	public void Close(int id) {
		KernelandProcess currentProcess = scheduler.getCurrentlyRunning();
		int[] cpDeviceIds = currentProcess.getDeviceIds();
		int vfsId = cpDeviceIds[id];
		cpDeviceIds[id] = -1;
		currentProcess.setDeviceIds(cpDeviceIds);
		vfs.Close(vfsId);
	}

	/*
	 * Calls read on the specified device
	 * 
	 * @Param: int id, int size
	 * 
	 * @Return: byte[]
	 */
	@Override
	public byte[] Read(int id, int size) {
		KernelandProcess currentProcess = scheduler.getCurrentlyRunning();
		int[] cpDeviceIds = currentProcess.getDeviceIds();
		int vfsId = cpDeviceIds[id];
		byte[] bArray = this.vfs.Read(vfsId, size);
		return bArray;
	}

	/*
	 * Calls write on the specified device
	 * 
	 * @Param: int id, byte[] data
	 * 
	 * @Return: length of data array
	 */
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

	/*
	 * Calls seek on the specified device
	 * 
	 * @Param: int id, int to
	 */
	@Override
	public void Seek(int id, int to) {
		KernelandProcess currentProcess = scheduler.getCurrentlyRunning();
		int[] cpDeviceIds = currentProcess.getDeviceIds();

		int vfsId = cpDeviceIds[id];
		this.vfs.Seek(vfsId, to);
	}

	public int getPid() {
		return scheduler.getPid();
	}

	public int getPidByName(String processName) {
		return scheduler.getPidByName(processName);
	}

	public void sendMessage(KernelMessage msg) {
		scheduler.sendMessage(msg);
	}

	public KernelMessage waitForMessage() {
		return scheduler.waitForMessage();
	}
}
