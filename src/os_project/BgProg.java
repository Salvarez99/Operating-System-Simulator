package os_project;

public class BgProg extends UserlandProcess{

	@Override
	public void run() {
		//        for(int i = 0; i < 10; i++){
		//            System.out.println("I'm in the background Ooooooo");
		//        }

		int ids[] = new int[10];
		for(int i = 0; i < 10; i++){
			int id = OS.Open("random " + (i+1000));
			byte[] bArray = OS.Read(id, 5);
			
			for (int j = 0; j < bArray.length; j++) {
				System.out.print(bArray[j] + " ");
			}
			System.out.println();
			ids[i] = id;
		}
		
		for (int i = 0; i < ids.length; i++) {
			OS.Close(ids[i]);
		}
		
	}

}
