package os_project;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HelloWorld helloworld = new HelloWorld();
		GoodbyeWorld goodbyeWorld = new GoodbyeWorld();
		
		OS.startUp(helloworld);
		OS.createProcess(goodbyeWorld);
		
	}

}
