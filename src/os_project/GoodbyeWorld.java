package os_project;

public class GoodbyeWorld extends UserlandProcess {

	public GoodbyeWorld() {
	}
	
	@Override
	public void run() {
        int startPtr = OS.allocateMemory(1024*100);
		
		// try {
		// 	while(true) {
		// 		System.out.println("Goodbye World");
		// 		Thread.sleep(50); // sleep for 50 ms
		// 	}
		// } catch (Exception e) { }

		
		
	}
}
