package os_project;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device {

    private RandomAccessFile[] fileArray;

    public FakeFileSystem() {
        this.fileArray = new RandomAccessFile[10];
    }

    @Override
    public int Open(String s) {
        int id;

        if (!s.isEmpty() || s != null) {
            try {
                try (RandomAccessFile newFile = new RandomAccessFile(s, "rw")) {
                    for (int i = 0; i < fileArray.length; i++) {
                        if (fileArray[i] == null) {
                            fileArray[i] = newFile;
                            id = i;
                            return id;
                        }
                    }
                }
            } catch (Exception e) {
                return -1;
            }
        }

        return -1;
    }

    @Override
    public void Close(int id) {
        try {
            fileArray[id].close();
            fileArray[id] = null;
        } catch (IOException e) {
            System.out.println("Error occured in FFS.Close()");
        }
    }

    @Override
    public byte[] Read(int id, int size) {
        byte[] bArray = new byte[size];

        try {
            fileArray[id].read(bArray);
        } catch (IOException e) {
            System.out.println("Error occured in FFS.Read()");
        }

        return bArray;
    }

    // TODO:Check
    @Override
    public int Write(int id, byte[] data) {

        if (data.length > 0) {
            try {
                this.fileArray[id].write(data);
                return data.length;

            } catch (IOException e) {
                System.out.println("Error occured in FFS.Write()");
            }
        }

        return 0;
    }

    // TODO: Check
    @Override
    public void Seek(int id, int to) {
        try {
            this.fileArray[id].seek(to);
        } catch (IOException e) {
            System.out.println("Error occured in FFS.Seek()");
        }
    }
}