package os_project;

public class Sleep500 extends UserlandProcess{
	
	@Override
	public void run() {
		System.out.println("I sleep");
		OS.sleep(500);
		System.out.println("I awake");
		
	}
	

}
