package os_project;

import java.util.Iterator;

public class World extends UserlandProcess{
    public World() {
	}
	
	@Override
	public void run() {
		
		try {
			while(true) {
				System.out.println(" World");
				OS.sleep(50); // sleep for 50 ms
			}
		} catch (Exception e) { }

		
	}
}
