package os_project;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device {

    private RandomAccessFile[] fileArray;

    public FakeFileSystem() {
        this.fileArray = new RandomAccessFile[10];
    }

    // TODO: Ask about throwing here
    @Override
    public int Open(String s) {
        int id;

        if (s.isEmpty() || s == null) {
            try {
                throw new Exception("filename is empty or null");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            RandomAccessFile newFile = new RandomAccessFile(s, "rw");

            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i] == null) {
                    fileArray[i] = newFile;
                    id = i;
                    return id;
                }
            }
        } catch (Exception e) {
            return -1;
        }

        return -1;
    }

    // TODO: May or may not be correct
    @Override
    public void Close(int id) {
        try {
            fileArray[id].close();
        } catch (IOException e) {
            System.out.println("Error occured in FFS.Close()");
        }

        for (int i = 0; i < this.fileArray.length; i++) {
            fileArray[i] = null;
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

    // TODO:May or may not be correct
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

    // TODO: Not sure what to do
    @Override
    public void Seek(int id, int to) {

        try {
            this.fileArray[id].seek(to);
        } catch (IOException e) {
            System.out.println("Error occured in FFS.Seek()");
        }
    }
}