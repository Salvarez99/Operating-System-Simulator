package os_project;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	private LinkedList<KernelandProcess> kernelandProcessList = new LinkedList<>();
	private Timer timer;
	private KernelandProcess kernelandProcess;
	private Scheduler scheduler;
	
	private class Interrupt extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			scheduler.switchProcess();
			
		}
		
	}
	
	
	
	
	
	public Scheduler() {
		//schedule interrupt using timer class
		timer.schedule(null, 250, 250);
	}
	
	//unsure
	public int createProcess(UserlandProcess up) {
		
		return 0;
	}
	
	//unsure
	public void switchProcess() {
		
	}
	
	
	
}
