package os_project;

public class ItProg extends UserlandProcess{

    @Override
    public void run() {

        // int x = 0;
        // while(x < 100){
            for(int i = 0; i < 10; i++){
                System.out.println("Give mo money");
             
                OS.sleep(50);
            }
            // x++;
        // }
    }
    
}
