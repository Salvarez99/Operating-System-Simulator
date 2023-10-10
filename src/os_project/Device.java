package os_project;

public interface Device {
	int Open(String s);

	void Close(int id);

	byte[] Read(int id, int size);

	int Write(int id, byte[] data);

	void Seek(int id, int to);
}
