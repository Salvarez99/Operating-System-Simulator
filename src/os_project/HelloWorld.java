package os_project;

import java.nio.charset.StandardCharsets;

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
		bArray = OS.Read(id, 10);
		String s = new String(bArray);
		System.out.println("Read: " + s);
		OS.Close(id);
	}
}