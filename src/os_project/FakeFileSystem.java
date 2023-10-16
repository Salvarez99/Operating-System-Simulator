package os_project;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device {

	private RandomAccessFile[] fileArray;

	public FakeFileSystem() {
		this.fileArray = new RandomAccessFile[10];
	}

    /*
     * Uses string input to create a new RandomAccessFile. Then places the file into the fileArray and returns
     * it's index position
     * @Param: String s
     * @Return: int id, index position of file
     */
	@Override
	public int Open(String s) {
		int id;

		if (!s.isEmpty() || s != null) {
			try {
				RandomAccessFile newFile = new RandomAccessFile(s, "rw");
						for (int i = 0; i < fileArray.length; i++) {
							if (fileArray[i] == null) {
								fileArray[i] = newFile;
								id = i;
								System.out.println("FFS Open: file array[" + id + "]");
								return id;
							}
						}

			} catch (Exception e) {
				return -1;
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
		try {
			System.out.println("FFS Close: file array[" + id + "]");
			fileArray[id].close();
			fileArray[id] = null;
		} catch (IOException e) {
			System.out.println("Error occured in FFS.Close()");
		}
	}

    /*
     * Calls RandomAccessFile read() on specified file
     * @Param: int id, int size
     * @Return: byte[]
     */
	@Override
	public byte[] Read(int id, int size) {
		byte[] bArray = new byte[size];

		try {
			System.out.println("FFS Read: file array[" + id + "]");
			fileArray[id].read(bArray);
		} catch (IOException e) {
			System.out.println("Error occured in FFS.Read()");
		}

		return bArray;
	}

    /*
     * Calls RandomAccessFile write() on specified file
     * @Param: int id, byte[] data
     * @Return: length of data array
     */
	@Override
	public int Write(int id, byte[] data) {

		if (data.length > 0) {
			try {
				System.out.println("FFS Write: file array[" + id + "]");
				RandomAccessFile file = this.fileArray[id];
				file.write(data);
				return data.length;

			} catch (IOException e) {
				System.out.println("Error occured in FFS.Write()");
			}
		}

		return 0;
	}

    /*
     * Calls RandomAccessFile seek() on specified file
     * @Param: int id, int to
     */
	@Override
	public void Seek(int id, int to) {
		try {
			System.out.println("FFS Seek: file array[" + id + "]");
			this.fileArray[id].seek(to);
		} catch (IOException e) {
			System.out.println("Error occured in FFS.Seek()");
		}
	}
}