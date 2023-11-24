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
		 * OUTDATED 
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

		//TODO: Implement allocate memory 
		/*
		 * Create instances of VTPM (One for every page the user is allocating)
		 * Populate KLP array
		 * 		If the element is not null (the allocated space is in use)
		 * 		
		 * 		Detecting contigouos blocks is based on presence of VTPM in array
		 */
		System.out.println("========================Allocating Memory=====================");
		ArrayList<Integer> availableSpaceIndices = new ArrayList<>();
		ArrayList<Integer> contiguousIndicesMemMap = new ArrayList<>();
		VirtualToMemoryMapping[] memMap = Kernel.scheduler.getCurrentlyRunning().getMemoryMap();
		int pages_needed = size / 1024;
		int free = 0;

		// System.out.println("Pages Needed: " + pages_needed);

		// true: space is free
		// false: space is occupied
		// finding available spaces in freespace[]
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

			// finding contigouos space in memMap[]
			for (int i = 0; i < memMap.length; i++) {

				if (contiguousSpace < pages_needed) {

					if (memMap[i] == null) {
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

			// mapping spaces together
			for (int i = 0; i < pages_needed; i++) {
				int value = availableSpaceIndices.remove(0);
				VirtualToMemoryMapping vtpm = new VirtualToMemoryMapping();
				vtpm.setPhysical_page_number(value);

				//ASK if on disk page number is the same as freespace[] entry: elaborate
				vtpm.setOn_disk_page_number(-1);

				freeSpace[value] = false;
				memMap[contiguousIndicesMemMap.remove(0)] = vtpm;
			}

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

		//TODO: Implement free memory
		/*
		 * Check if the physical page is not -1 before updating the physical memory in
		 * use
		 * 
		 * Set array entry for each block back to null (freeing the VTPM)
		 */
		System.out.println("========================Freeing Memory========================");

		VirtualToMemoryMapping[] memMap = Kernel.scheduler.getCurrentlyRunning().getMemoryMap();
		int start = pointer / 1024;
		int offset = size / 1024;
		int end = start + offset;
		int occupied = 0;

		//Finding occupied indices
		for (int i = 0; i < memMap.length; i++) {
			if (memMap[i] != null) {
				occupied++;
			}
		}

		//Freeing occupied indices
		for (int i = 0; i < end; i++) {
			if (memMap[i] != null) {
				int freeSpaceIndex = memMap[i].getPhysical_page_number();
				freeSpace[freeSpaceIndex] = true;
				memMap[i] = null;
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

		//TODO: Implement get mapping
		/*
		 * Find memory map entry as before
		 * If physical page = -1: 
		 * 		Find physical page in the "in-use" array and assign it 
		 * Elif none are available:
		 * 		Do page swap to free one up
		 */
		KernelandProcess currentProcess = Kernel.scheduler.getCurrentlyRunning();
		VirtualToMemoryMapping[] memMap = currentProcess.getMemoryMap();
		VirtualToMemoryMapping vtmp = memMap[virtualPageNumber];
		int physical_page_number = vtmp.getPhysical_page_number();

		Random rand = new Random();
		int TLB_index = rand.nextInt(2);
		int[][] TLB = UserlandProcess.getTLB();
		byte[] data = new byte[1024];
		int counter = 0;

		TLB[TLB_index][0] = virtualPageNumber;
		TLB[TLB_index][1] = physical_page_number;

		//I believe I need a loop to keep page swapping
		if(physical_page_number == -1){

			//Looking for a free page then assigning it to TLB
			for (int i = 0; i < freeSpace.length; i++) {

				if(freeSpace[i] == false){
					TLB[TLB_index][1] = physical_page_number;
					break;

				}else
					counter++;
			}

			
			//No entry was free
			if(counter == freeSpace.length - 1){
				/*
				* Find a random process
				* Pageswap
				*/
				Kernel.scheduler.pageSwap();

			}else{
			//An entry was free to use
				if(vtmp.getOn_disk_page_number() != -1){
				//data was previously written to disk
					/*
					 * load old data in
					 * Populate the physical page
					 */

					int start = vtmp.getOn_disk_page_number() * 1024;
					int index = 0;
					OS.Seek(OS.getId(), start);
					data = OS.Read(OS.getId(), 1024);
					scheduler.getCurrentlyRunning().getMemoryMap()[virtualPageNumber].physical_page_number = vtmp.physical_page_number;

					for (int i = start; i < start + 1024; i++) {
						UserlandProcess.physicalMemory[i] = data[index];
						index++;
					}
				}else{
				//data was not previously written to disk
					/*
					 * Populate the memory with 0's
					 */

					int physical_address = vtmp.getPhysical_page_number() * 1024;
					int index = 0;
					for (int i = physical_address; i < physical_address + 1024; i++) {
						data[index] = UserlandProcess.physicalMemory[i];
						UserlandProcess.physicalMemory[i] = 0;
						index++;
					}


					

				}
			}
		}
	}

	public VFS getVfs() {
		return vfs;
	}
}
