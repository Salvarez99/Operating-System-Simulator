package os_project;

public class Main {

	public static void main(String[] args) {
		
		UserlandProcess helloworld = new HelloWorld();
		UserlandProcess goodbyeWorld = new GoodbyeWorld();
		UserlandProcess realProgram = new RtProg();
		UserlandProcess interactiveProgram = new ItProg();
		UserlandProcess backgroundProgram = new BgProg();
		UserlandProcess sleepingProcess = new Sleep500();



		
		//PID 1
		//Runs long time intentionally
		OS.startUp(interactiveProgram);
		//PID 2
		OS.createProcess(realProgram, Priority.REALTIME);
		//PID 3
		OS.createProcess(sleepingProcess, Priority.INTERACTIVE);
		//PID 4
		OS.createProcess(backgroundProgram,Priority.BACKGROUND);
		

	}
}
