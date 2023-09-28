package os_project;

public class Main {

	public static void main(String[] args) {
		
		UserlandProcess helloworld = new HelloWorld();
		UserlandProcess goodbyeWorld = new GoodbyeWorld();
		UserlandProcess world = new World();
		UserlandProcess slep = new Sleep500();
		
		//PID 1
		//Runs long time intentionally
		OS.startUp(helloworld);
		
		//PID 2
		//Runs long time intentionally 
		// OS.createProcess(goodbyeWorld, Priority.REALTIME); 
		
		//PID 3
		//RealTime process runs long time but calls OS.sleep
		// OS.createProcess(world, Priority.REALTIME);
		
		//PID 4
		//Interactive process runs long time but calls OS.sleep
		// OS.createProcess(world, Priority.INTERACTIVE);
		
		//PID 5
		//Background process runs long time but calls OS.sleep
		// OS.createProcess(world, Priority.BACKGROUND);
		
		//PID 6
		//Process that calls OS.sleep intentionally
		OS.createProcess(slep, Priority.INTERACTIVE);
	}
}
