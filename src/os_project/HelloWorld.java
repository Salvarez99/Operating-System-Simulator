package os_project;

public class HelloWorld extends UserlandProcess{

	public HelloWorld() {
	}
	
	@Override
	public void run() {
		
		try {
			while(true) {
				System.out.println("Hello World");
				Thread.sleep(50); // sleep for 50 ms
			}
		} catch (Exception e) { }
		
	}
}