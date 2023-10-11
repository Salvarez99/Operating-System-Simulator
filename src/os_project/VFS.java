package os_project;

public class VFS implements Device {
    private DevicePair[] devices;

    public VFS() {
        this.devices = new DevicePair[10];
    }

    // TODO: may not need to set pair device and id
    @Override
    public int Open(String s) {
        String[] strings = s.split(" ");
        DevicePair pair = new DevicePair();
        int id;

        if (strings[0].equals("random")) {
            RandomDevice device = new RandomDevice();
            id = device.Open(strings[1]);
            pair.setDevice(device);
            pair.setId(id);

            if (search(pair)) {
                return id;
            }

        } else if (strings[0].equals("file")) {
            FakeFileSystem device = new FakeFileSystem();
            id = device.Open(strings[1]);
            pair.setDevice(device);
            pair.setId(id);

            if (search(pair)) {
                return id;
            }
        }
        return -1;
    }

    @Override
    public void Close(int id) {
        this.devices[id] = null;
    }

    // Hopefully passing to proper device
    @Override
    public byte[] Read(int id, int size) {
        Device dev = this.devices[id].getDevice();
        int devId = this.devices[id].getId();
        byte[] bArray = dev.Read(devId, size);
        return bArray;
    }

    // Hopefully passing to proper device
    @Override
    public int Write(int id, byte[] data) {

        if (data.length > 0) {
            Device dev = this.devices[id].getDevice();
            int devId = this.devices[id].getId();
            dev.Write(devId, data);
            return data.length;
        }

        return 0;
    }

    // Hopefully passing to proper device
    @Override
    public void Seek(int id, int to) {
        Device dev = this.devices[id].getDevice();
        int devId = this.devices[id].getId();
        dev.Seek(devId, to);
    }

    private boolean search(DevicePair pair) {
        for (int i = 0; i < devices.length; i++) {
            if (devices[i] == null) {
                devices[i] = pair;
                return true;
            }
        }
        return false;
    }
}