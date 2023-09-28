package os_project;

public class BgProg extends UserlandProcess{

    @Override
    public void run() {
        for(int i = 0; i < 10; i++){
            System.out.println("I'm in the background Ooooooo");
        }
    }
    
}
