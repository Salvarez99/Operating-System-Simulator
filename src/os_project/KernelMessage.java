package os_project;

public class KernelMessage {
    private int senderPid;
    private int targetPid;
    private int what;
    private byte[] data;

    KernelMessage(KernelMessage message) {
        this.senderPid = message.senderPid;
        this.targetPid = message.targetPid;
        this.what = message.what;
        this.data = message.data.clone();

    }

    public KernelMessage(int targetPid, byte[] data, int what) {
        this.targetPid = targetPid;
        this.data = data;
        this.what = what;
    }

    public int getSenderPid() {
        return senderPid;
    }

    public void setSenderPid(int senderPid) {
        this.senderPid = senderPid;
    }

    public int getTargetPid() {
        return targetPid;
    }

    public void setTargetPid(int targetPid) {
        this.targetPid = targetPid;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String output = "";

        output += "\nSender pid (" + this.senderPid + ")\n";
        output += "Target pid (" + this.targetPid + ")\n";
        output += "What: " + this.what;

        return output;
    }
}