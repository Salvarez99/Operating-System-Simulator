package os_project;

public class World extends UserlandProcess{
    public World() {
	}
	
	@Override
	public void run() {
		
		try {
			while(true) {
				System.out.println("World");
				Thread.sleep(50); // sleep for 50 ms
			}
		} catch (Exception e) { }
		
	}
}
