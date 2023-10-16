package os_project;

public class VFS implements Device {
    private DevicePair[] devices;
    private RandomDevice randDevice;
    private FakeFileSystem fakeFileSys;

    public VFS() {
        this.devices = new DevicePair[10];
        this.randDevice = new RandomDevice();
        this.fakeFileSys = new FakeFileSystem();
    }

    /*
     * Uses string input to decide which device to open, then calls respective open on it. 
     * Updates device list with new opened device and returns it's index
     * @Param: String s
     * @Return: int id, device index position
     */
    @Override
    public int Open(String s) {
        String[] strings = s.split(" ");
        DevicePair pair = new DevicePair();
        int id;

        if (strings[0].equals("random")) {
            id = this.randDevice.Open(strings[1]);
            pair = new DevicePair(this.randDevice, id);

            if (search(pair)) {
                return id;
            }

        } else if (strings[0].equals("file")) {
            id = this.fakeFileSys.Open(strings[1]);
            pair = new DevicePair(this.fakeFileSys, id);

            if (search(pair)) {
                return id;
            }
        }
        return -1;
    }

    /*
     * Uses passed id to call close on specified device and set device's index position to null 
     * in the device list
     * @Param: int id
     */
    @Override
    public void Close(int id) {
    	this.devices[id].getDevice().Close(id);
        this.devices[id] = null;
    }

    /*
     * Uses passed if to call respective read on the specified device.
     * @Param: int id, int size
     * @Return: byte[]
     */
    @Override
    public byte[] Read(int id, int size) {
        Device dev = this.devices[id].getDevice();
        int devId = this.devices[id].getId();
        byte[] bArray = dev.Read(devId, size);
        return bArray;
    }

    /*
     * Uses passed if to call respective write on the specified device.
     * @Param: int id, byte[] dat
     * @Return: length of data array
     */
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

    /*
     * Uses passed if to call respective seek on the specified device.
     * @Param: int id, int to
     */
    @Override
    public void Seek(int id, int to) {
        Device dev = this.devices[id].getDevice();
        int devId = this.devices[id].getId();
        dev.Seek(devId, to);
    }

    /*
     * Searches for an empty index in Device pair list and inserts device pair.
     * Returns true if pair was added successfully
     * @Param: DevicePair pair
     * @Return: boolean
     */
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