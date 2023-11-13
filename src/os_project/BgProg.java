package os_project;

public class BgProg extends UserlandProcess{

	@Override
	public void run() {

		int ids[] = new int[10];

		int pointer = OS.allocateMemory(1024);
		System.out.println("pointer: " + pointer);
		byte data = 98;
		write(pointer, data);
		read(pointer);

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
