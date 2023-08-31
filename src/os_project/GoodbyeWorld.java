package os_project;

public class GoodbyeWorld extends UserlandProcess {

	public GoodbyeWorld() {
	}
	
	@Override
	public void run() {
		while(true) {
			System.out.println("Goodbye World");
		}
	}
}
