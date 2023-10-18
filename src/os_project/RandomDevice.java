package os_project;

import java.util.Random;

public class RandomDevice implements Device {

    private Random[] devices;

    public RandomDevice() {
        this.devices = new Random[10];
    }

    /*
     * Uses string input as seed to create a new Random object. Then places the Random object into the devices array 
     * and returns it's index position
     * @Param: String s
     * @Return: int id, index position of file
     */
    @Override
    public int Open(String s) {

        Random device = new Random();
        int id;

        if (!s.isEmpty() || s != null) {

            int seed = Integer.valueOf(s);
            device = new Random(seed);
        }

        for (int i = 0; i < this.devices.length; i++) {

            if (devices[i] == null) {
                devices[i] = device;
                id = i;
                System.out.println("Random Open: rdArray[" + id + "]");
                return id;
            }

        }
        return -1;
    }

    /*
     * Uses passed id to close specified RandomAccessFile. Then sets it's index position to null
     * @Param: int id
     */
    @Override
    public void Close(int id) {
        System.out.println("Random Close: rdArray[" + id + "]");
        this.devices[id] = null;
    }

    /*
     * Calls Random.nextBytes() and fills an array with random bytes then
     * returns the byte array
     * @Param: int id, int size
     * @Return: byte[]
     */
    @Override
    public byte[] Read(int id, int size) {
        byte[] bArray = new byte[size];
        System.out.println("Random Read: rdArray[" + id + "]");
        this.devices[id].nextBytes(bArray);

        return bArray;
    }

    /*
     * returns the length of the byte array
     * @Param: int id, byte[] data
     * @Return: int, size of byte array
     */
    @Override
    public int Write(int id, byte[] data) {
        System.out.println("Random Write: rdArray[" + id + "]");
        return data.length;
    }

    /*
     * Calls read on specified randomDevice
     * @Param: int id, int to
     */
    @Override
    public void Seek(int id, int to) {
        System.out.println("Random Read: rdArray[" + id + "]");
        Read(id, to);
    }
}
