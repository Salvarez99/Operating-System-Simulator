package os_project;

import java.util.Iterator;

public class World extends UserlandProcess{
    public World() {
	}
	
	@Override
	public void run() {
		
		System.out.println("world");
		
		OS.sleep(250);
		
		for (int i = 0; i < 5; i++) {
			System.out.println("world " + (i + 1));
		}
		
	}
}
