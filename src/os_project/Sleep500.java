package os_project;

public class Sleep500 extends UserlandProcess{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("I sleep");
		OS.sleep(500);
		System.out.println("I awake");
		
	}
	

}
