package os_project;

import java.util.Random;

public class RandomDevice implements Device {

    private Random[] devices;

    public RandomDevice() {
        this.devices = new Random[10];
    }

    @Override
    public int Open(String s) {

        Random device;
        int id;

        if (!s.isEmpty() || s != null) {

            int seed = Integer.valueOf(s);
            device = new Random(seed);
        }

        device = new Random();

        for (int i = 0; i < this.devices.length; i++) {

            if (devices[i] == null) {

                devices[i] = device;
                id = i;
                return id;
            }

        }
        return -1;
    }

    @Override
    public void Close(int id) {
        this.devices[id] = null;
    }

    // TODO: Check
    @Override
    public byte[] Read(int id, int size) {
        byte[] bArray = new byte[size];

        this.devices[id].nextBytes(bArray);

        return bArray;
    }

    @Override
    public int Write(int id, byte[] data) {

        return data.length;
    }

    // TODO: Check
    @Override
    public void Seek(int id, int to) {
        
        Read(id,to);

    }
}
