package os_project;

public class ItProg extends UserlandProcess{

	@Override
	public void run() {
		//            for(int i = 0; i < 10; i++){
		//                System.out.println("Give mo money");
		//             
		//                OS.sleep(50);
		//            }
		int ids[] = new int[11];
		for(int i = 0; i < 11; i++){
			int id = OS.Open("random " + (i+200));
			System.out.println("itProg Open id returned: "+ id);
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
