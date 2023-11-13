package os_project;

public class RtProg extends UserlandProcess{

    @Override
    public void run() {
        
//        for(int i = 0; i < 10; i++){
//            System.out.println("Give me a raise");
//            OS.sleep(50);
//        }

		int id = OS.Open("random 100");
		byte[] bArray = OS.Read(id, 5);
		int pointer = OS.allocateMemory(10240);
		byte data = 42;

		while (data > 0){
			write(pointer, data);
			data--;
			read(pointer);
			pointer += 50;
		}


		
		for (int i = 0; i < bArray.length; i++) {
			System.out.print(bArray[i]);
		}
		System.out.println();
		
		OS.Close(id);
    	
    }
}
