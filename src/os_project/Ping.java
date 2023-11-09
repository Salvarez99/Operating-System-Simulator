package os_project;

public class Ping extends UserlandProcess {

    @Override
    public void run() {
        String className = getClass().getSimpleName();
        byte[] data = "ping".getBytes();
        OS.sleep(50);
        int targetProcessPid = OS.getPidByName("Pong");
        int i = 0;
        KernelMessage message = new KernelMessage(targetProcessPid, data, i);

        System.out.println("I am " + className + ", Pong pid: " + targetProcessPid);

        while(true){
            // for (int i = 0; i < 3; i++) {
    
                System.out.printf("\n%s: from %d to %d: what: $d\n", 
                className, OS.getPidByName("Pong"), targetProcessPid, message.getWhat());
                OS.sendMessage(message);
                message = OS.waitForMessage();
                message.setTargetPid(targetProcessPid);
                message.setData(data);
                message.setWhat(i++);
            // }

        }
    }
}
