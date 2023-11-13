package os_project;

public class Main {

	public static void main(String[] args) {

		UserlandProcess helloworld = new HelloWorld();
		UserlandProcess goodbyeWorld = new GoodbyeWorld();
		UserlandProcess realProgram = new RtProg();
		UserlandProcess interactiveProgram = new ItProg();
		UserlandProcess backgroundProgram = new BgProg();
		UserlandProcess sleepingProcess = new Sleep500();
		UserlandProcess pingProcess = new Ping();
		UserlandProcess pongProcess = new Pong();
		UserlandProcess pageProcess = new Paging();

		// PID 1
		// OS.startUp(helloworld);
		OS.startUp(pageProcess);

		// PID 2
		OS.createProcess(realProgram, Priority.REALTIME);
		// PID 3
		// OS.createProcess(sleepingProcess, Priority.INTERACTIVE);
		// PID 4
		OS.createProcess(backgroundProgram, Priority.BACKGROUND);

		// OS.createProcess(pageProcess, Priority.INTERACTIVE);
		// PID 5
		// OS.createProcess(interactiveProgram, Priority.INTERACTIVE);
		// PID 6
		// OS.createProcess(pingProcess, Priority.INTERACTIVE);
		// PID 7
		// OS.createProcess(pongProcess, Priority.INTERACTIVE);

	}
}
