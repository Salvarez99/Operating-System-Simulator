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

		/*
		 * Create instances of VTPM (One for every page the user is allocating)
		 * Populate KLP array (MemMap)
		 * 		If the element is not null (the allocated space is in use)
		 * 		
		 * 		Detecting contiguous blocks is based on presence of VTPM in array
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

			// finding contiguous space in memMap[]
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
				//Allocating new memory, shouldnt be written to disk yet
				vtpm.setOn_disk_page_number(-1);
				System.out.println(vtpm);

				freeSpace[value] = false;
				memMap[contiguousIndicesMemMap.remove(0)] = vtpm;
			}

			// System.out.println("Start Address: " + startVirtualAddress);
			System.out.println("=====================DONE: Allocating Memory==================");
			System.out.println("Start virtual address: " + startVirtualAddress);
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
				int physicalPageNum = memMap[i].getPhysical_page_number();

				if(physicalPageNum != -1){
					freeSpace[physicalPageNum] = true;

					int startPhysicalMem = physicalPageNum * 1024;
					int endPhysicalMem = startPhysicalMem + 1024;
					// int counter = 0;

					//Freeing physical memory
					// System.out.println("Starting index: " + (physicalPageNum * 1024));
					// System.out.println("Ending index: " + ((physicalPageNum * 1024) + 1024));


					for (int index = startPhysicalMem; index <endPhysicalMem; index++) {
						UserlandProcess.physicalMemory[index] = 0;
						// counter++;
					}

					// System.out.println("Counter: "+ counter);
				}
				memMap[i] = null;
				occupied--;
			}
		}

		System.out.println("Occupied: " + occupied);
		if (occupied == 0){

			System.out.println("=====================DONE: Freeing Memory====================");
			return true;
		}

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
		System.out.println("=====================GetMapping()=====================");
		KernelandProcess currentProcess = Kernel.scheduler.getCurrentlyRunning();
		VirtualToMemoryMapping[] memMap = currentProcess.getMemoryMap();
		VirtualToMemoryMapping vtmp = memMap[virtualPageNumber];

		System.out.println("VP: " + virtualPageNumber);
		System.out.println(vtmp);
		int physical_page_number = vtmp.getPhysical_page_number();

		Random rand = new Random();
		int TLB_index = rand.nextInt(2);
		int[][] TLB = UserlandProcess.getTLB();

		TLB[TLB_index][0] = virtualPageNumber;
		TLB[TLB_index][1] = physical_page_number;

		int availableSpaces = 0;


		if(physical_page_number == -1){

			//Looking for a free space in the freeSpace[]
			for (int i = 0; i < freeSpace.length; i++) {

				//Checking for first available space
				if(freeSpace[i] == true){
					physical_page_number = i;
					vtmp.setPhysical_page_number(physical_page_number);
					availableSpaces++;
					break;
				}
			}

			//Checking to see if no spaces were available
			if(availableSpaces == 0){
				//swap

				boolean hasPhysicalPage = false;
				
				while(!hasPhysicalPage){
					KernelandProcess randomProcess = scheduler.getRandomProcess();
					VirtualToMemoryMapping[] rpMemMap = randomProcess.getMemoryMap();
					
					for(int i = 0; i < rpMemMap.length; i++){

						//Checking to see if element has physical page
						if(rpMemMap[i].getPhysical_page_number() > -1){
							/*
							 * write victim page to disk
							 * assign new block to swap file, if they didnt have one already (check ondiskpagenum)
							 * set victims physical page num to -1
							 * current physical page num to victim's old num
							 */

							int rpPhysicalPageNum = rpMemMap[i].getPhysical_page_number();
							int rpDiskPageNum = rpMemMap[i].getOn_disk_page_number();
							byte[] rpPhysicalMemory = new byte[1024];
							int rpStart = rpPhysicalPageNum * 1024;
							int index = 0;
							hasPhysicalPage = true;


							//Getting random process' memory to store onto disk
							for (int j = rpStart; j < (rpStart + 1024); j++) {
								rpPhysicalMemory[index] = UserlandProcess.physicalMemory[i];
								index++;
							}
							
							//Check if random process' physical page has dedicated page on disk
							if(rpMemMap[i].getOn_disk_page_number() > -1){
								//Writing victim's page to disk
								OS.Seek(OS.swapId, rpDiskPageNum * 1024);
								OS.Write(OS.swapId, rpPhysicalMemory);

							}else{
								//Writing victim's page to disk
								OS.Seek(OS.swapId, OS.swapPageNum * 1024);
								OS.Write(OS.swapId, rpPhysicalMemory);
								rpMemMap[i].setOn_disk_page_number(OS.swapPageNum);
								OS.swapPageNum++;
							}
							
							vtmp.setPhysical_page_number(rpPhysicalPageNum);
							rpMemMap[i].setPhysical_page_number(-1);
							break;
						}
					}
				}
			}
		}

		if(physical_page_number > -1){

			int cpStart = physical_page_number * 1024;
			int cpOnDiskPageNum = vtmp.getOn_disk_page_number();

			if(cpOnDiskPageNum > -1){
				OS.Seek(OS.swapId, cpOnDiskPageNum * 1024);
				byte[] readMemory = OS.Read(OS.swapId, 1024);
				int index = 0;

				for (int j = cpStart; j < cpStart + 1024; j++) {
					UserlandProcess.physicalMemory[j] = readMemory[index];
					index++;
				}

			}else{
				for (int j = cpStart; j < (cpStart + 1024); j++) {
					UserlandProcess.physicalMemory[j] = 0;
				}
			}
		}
	}

	public VFS getVfs() {
		return vfs;
	}
}
