package os_project;

public class HelloWorld extends UserlandProcess{

	public HelloWorld() {
		run();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			System.out.println("Hello World");
		}
		
	}
}