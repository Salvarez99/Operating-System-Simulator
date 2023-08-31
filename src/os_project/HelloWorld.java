package os_project;

public class HelloWorld extends UserlandProcess{

	public HelloWorld() {
	}
	
	@Override
	public void run() {
		while(true) {
			System.out.println("Hello World");
		}
		
	}
}