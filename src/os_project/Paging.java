package os_project;

public class Paging extends UserlandProcess{

    @Override
    public void run() {
        int pointer = OS.allocateMemory(4096);
        System.out.println("Pointer: " + pointer);
        byte data = 5;

        read(0);
        write(0, data);
        // OS.freeMemory(pointer, 2048);
    }
}
