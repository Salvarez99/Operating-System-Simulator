package os_project;

public class Paging extends UserlandProcess{

    @Override
    public void run() {
        int startPtr = OS.allocateMemory(1024*100);
        int pointer = startPtr;
        // System.out.println("Pointer: " + pointer);
        byte data = 127;
        byte data1 = 51;       
        byte data2 = 108;

    
        // write(pointer, data);
        // byte read = read(pointer + 2049);

        // System.out.println("Pointer: " + pointer);
        // startPtr = OS.allocateMemory(3072);
        // System.out.println("Pointer: " + startPtr);







    }
}
