package os_project;

public class Main {

	public static void main(String[] args) {
		
		HelloWorld helloworld = new HelloWorld();
		GoodbyeWorld goodbyeWorld = new GoodbyeWorld();
		
		OS.startUp(helloworld);
		OS.createProcess(goodbyeWorld); //add priority to this
		/*added from eclipse */
	}

}
