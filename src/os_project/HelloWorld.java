package os_project;

public class HelloWorld extends UserlandProcess{

	public HelloWorld() {
	}
	
	@Override
	public void run() {
		// try {
		// 	while(true) {
		// 		System.out.println("Hello World");
		// 		Thread.sleep(50); // sleep for 50 ms

		// 	}
		// } catch (Exception e) { }

		byte[] bArray = "HelloWorld".getBytes();

		int id = OS.Open("file data.txt");
		OS.Write(id, bArray);
		OS.Seek(id, 5);
		bArray = OS.Read(id, 5);
		String s = new String(bArray);
		System.out.println("Read: " + s);
		OS.Close(id);
	}
}