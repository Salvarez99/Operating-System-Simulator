package os_project;

public class HelloWorld extends UserlandProcess{

	public HelloWorld() {
	}
	
	@Override
	public void run() {
		
		OS os = new OS();
		// try {
		// 	while(true) {
		// 		System.out.println("Hello World");
		// 		Thread.sleep(50); // sleep for 50 ms

		// 	}
		// } catch (Exception e) { }
		int id = os.Open("random 100");
		os.Read(id, 10);
		os.Close(id);
		
	}
}