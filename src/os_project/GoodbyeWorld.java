package os_project;

public class GoodbyeWorld extends UserlandProcess {

	public GoodbyeWorld() {
		run();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			System.out.println("Goodbye World");
		}
	}
}
