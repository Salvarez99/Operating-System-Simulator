package os_project;

public class Main {

	public static void main(String[] args) {
		
		HelloWorld helloworld = new HelloWorld();
		GoodbyeWorld goodbyeWorld = new GoodbyeWorld();
		World world = new World();
		
		OS.startUp(helloworld);
		OS.createProcess(goodbyeWorld, Priority.BACKGROUND); 
		OS.createProcess(world, Priority.REALTIME);
	}
}
