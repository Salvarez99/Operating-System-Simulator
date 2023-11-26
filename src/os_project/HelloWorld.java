package os_project;

public class HelloWorld extends UserlandProcess {

	public HelloWorld() {
	}

	@Override
	public void run() {
		// try {
		// while(true) {
		// System.out.println("Hello World");
		// Thread.sleep(50); // sleep for 50 ms

		// }
		// } catch (Exception e) { }
        int startPtr = OS.allocateMemory(1024*100);

		// byte[] bArray = "HelloWorld".getBytes();

		// int id = OS.Open("file data.txt");
		// OS.Write(id, bArray);
		// OS.Seek(id, 5);
		// bArray = OS.Read(id, 5);
		// String s = new String(bArray);
		// System.out.println("Read: " + s);

		// System.out.println("Simple class name: " + getClass().getSimpleName());

		// OS.Close(id);
	}
}