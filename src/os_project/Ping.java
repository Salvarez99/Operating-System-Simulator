package os_project;

public class Ping extends UserlandProcess {

    @Override
    public void run() {
        String className = getClass().getSimpleName();
        byte[] data = "ping".getBytes();
        int targetProcessPid = OS.getPidByName("Pong");
        KernelMessage message = new KernelMessage(targetProcessPid, data, 0);

        System.out.println("I am" + className + ", Pong pid: " + targetProcessPid);
        for (int i = 0; i < 3; i++) {

            System.out.printf("\n%s: from %d to %d: what: $d\n", 
            className, OS.getPidByName(className), targetProcessPid, message.getWhat());
            OS.sendMessage(message);
            message = OS.waitForMessage();
            message.setTargetPid(targetProcessPid);
            message.setData(data);
            message.setWhat(i);
        }
    }
}
