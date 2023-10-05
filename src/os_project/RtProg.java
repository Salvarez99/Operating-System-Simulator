package os_project;

public class RtProg extends UserlandProcess{

    @Override
    public void run() {
        
        for(int i = 0; i < 10; i++){
            System.out.println("Give me a raise");
            OS.sleep(50);
        }
    }
}
