package os_project;

public class VFS implements Device {
    DevicePair[] devices;

    @Override
    public int Open(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Open'");
    }

    @Override
    public void Close(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Close'");
    }

    @Override
    public byte[] Read(int id, int size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Read'");
    }

    @Override
    public int Write(int id, byte[] data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Write'");
    }

    @Override
    public void Seek(int id, int to) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Seek'");
    }

}
