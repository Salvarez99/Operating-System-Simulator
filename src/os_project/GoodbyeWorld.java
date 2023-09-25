package os_project;

public class GoodbyeWorld extends UserlandProcess {

	public GoodbyeWorld() {
	}
	
	@Override
	public void run() {
		
		try {
			while(true) {
				System.out.println("Goodbye World");
				OS.sleep(50); // sleep for 50 ms
			}
		} catch (Exception e) { }

		
		
	}
}
