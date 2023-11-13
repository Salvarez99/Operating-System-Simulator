package os_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Kernel implements Device {
	private static Scheduler scheduler = new Scheduler();
	private VFS vfs = new VFS();
	// represents free spaces of physical memory
	private boolean[] freeSpace = new boolean[1000];

	public Kernel() {
		Arrays.fill(freeSpace, true);
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

	public int allocateMemory(int size) {
		/*
		 * call OS -> kernel
		 * 
		 * kernel:
		 * make sure divisible by 1024
		 * calc num of pages we are trying allocate
		 * amount of pages needed = size / 1024
		 * check if amount of free spaces are equal to the amount of pages needed
		 * if yes:
		 * check memoryMap for contiguous empty pages and is equal to pages needed
		 * if yes:
		 * take index of empty space in freeSpace and place that as the value in
		 * memoryMap
		 * 
		 * return start virtual address, where we are starting in memoryMap
		 */
		System.out.println("========================Allocating Memory=====================");
		ArrayList<Integer> availableSpaceIndices = new ArrayList<>();
		ArrayList<Integer> contiguousIndicesMemMap = new ArrayList<>();
		int[] memMap = Kernel.scheduler.getCurrentlyRunning().getMemoryMap();
		int pages_needed = size / 1024;
		int free = 0;

		// System.out.println("Pages Needed: " + pages_needed);

		// true: space is free
		// false: space is occupied
		for (int i = 0; i < this.freeSpace.length; i++) {
			if (freeSpace[i] == true && free <= pages_needed) {
				free++;
				availableSpaceIndices.add(i);
			}
		}

		// System.out.println("FreeSpaces Available:" + availableSpaceIndices);

		if (free >= pages_needed) {
			int contiguousSpace = 0;
			Integer startVirtualAddress = 0;

			for (int i = 0; i < memMap.length; i++) {

				if (contiguousSpace < pages_needed) {

					if (memMap[i] == -1) {
						contiguousSpace++;
						contiguousIndicesMemMap.add(i);
					} else {
						contiguousSpace--;
						if (!contiguousIndicesMemMap.isEmpty())
							contiguousIndicesMemMap.remove(0);
					}
				} else
					break;
			}
			// System.out.println("Contiguous Space: " + contiguousSpace);
			// System.out.println("contiguousIndicesMemMap Size: " +
			// contiguousIndicesMemMap.size());
			// System.out.println("contiguousIndicesMemMap : " + contiguousIndicesMemMap);

			startVirtualAddress = contiguousIndicesMemMap.get(0);
			for (int i = 0; i < pages_needed; i++) {
				int value = availableSpaceIndices.remove(0);
				// System.out.println("Value: " + value);
				// System.out.println("\nFreeSpace[" + value + "] = " + freeSpace[value]);
				freeSpace[value] = false;
				// System.out.println("\nFreeSpace[" + value + "] = " + freeSpace[value]);
				memMap[contiguousIndicesMemMap.remove(0)] = value;
			}
			;

			System.out.println("Start Address: " + startVirtualAddress);
			return startVirtualAddress;
		}
		return -1;
	}

	public boolean freeMemory(int pointer, int size) {
		/*
		 * takes virtual address and the amount to free
		 * return successful or not
		 * 
		 * Kernel:
		 * calc num of pages we are trying allocate
		 * amount of pages needed = size / 1024
		 * free up memory map from indices (pointer / 1024) to (amount of pages needed)
		 * and free the associated freeSpace index
		 * return true
		 * return false
		 */
		System.out.println("========================Freeing Memory========================");

		int[] memMap = Kernel.scheduler.getCurrentlyRunning().getMemoryMap();
		int start = pointer / 1024;
		int offset = size / 1024;
		int end = start + offset;
		int occupied = 0;

		for (int i = 0; i < memMap.length; i++) {
			if (memMap[i] > -1) {
				occupied++;
			}
		}

		for (int i = 0; i < end; i++) {
			if (memMap[i] > -1) {
				int freeSpaceIndex = memMap[i];
				freeSpace[freeSpaceIndex] = true;
				memMap[i] = -1;
				occupied--;
			}
		}

		System.out.println("Occupied: " + occupied);
		if (occupied == 0)
			return true;

		return false;
	}

	public void getMapping(int virtualPageNumber) {
		/*
		 * Update randomly one of the 2 TLB entries
		 * 
		 * random number val = index of TLB
		 * 
		 * get memoryMap
		 * index = virtualPageNum
		 * value at index = phys page num
		 * 
		 * update virt page num on TLB to = given VPN
		 * update phys page on TLB to = value at index
		 * 
		 */
		KernelandProcess currentProcess = Kernel.scheduler.getCurrentlyRunning();
		int[] memMap = currentProcess.getMemoryMap();
		int[][] TLB = UserlandProcess.getTLB();
		Random rand = new Random();
		int TLB_index = rand.nextInt(2);

		TLB[TLB_index][0] = virtualPageNumber;
		TLB[TLB_index][1] = memMap[virtualPageNumber];
	}
}
