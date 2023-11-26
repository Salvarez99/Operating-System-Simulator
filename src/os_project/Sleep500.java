package os_project;

public class Sleep500 extends UserlandProcess{
	
	@Override
	public void run() {
        int startPtr = OS.allocateMemory(1024*100);

		// System.out.println("I sleep");
		// OS.sleep(500);
		// System.out.println("I awake");
		
	}
	

}
