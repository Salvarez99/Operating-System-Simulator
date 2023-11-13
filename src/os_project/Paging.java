package os_project;

public class Paging extends UserlandProcess{

    @Override
    public void run() {
        int pointer = OS.allocateMemory(4096);
        // System.out.println("Pointer: " + pointer);
        byte data = 127;
        byte data1 = 51;       
        byte data2 = 108;


        write(pointer, data);
        byte read_data = read(pointer);

        write(pointer + 4096, data1);
        byte read_data1 = read(pointer + 4096);
        System.out.println("read_data: " + read_data);
        System.out.println("read_data1: " + read_data1);
        System.out.println("Page pointer " + pointer);

        OS.allocateMemory(1024);
        pointer += 5120;

        write(pointer, data2);
        byte read_data2 = read(pointer);
        System.out.println("read_data2: " + read_data2);


    }
}
