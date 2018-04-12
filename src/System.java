import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class System {

    private List<Device> devices;
    private Queue<Requirement> queueWaiting;
    private Queue<Requirement> queueServed;
    private int sources;

    public System(Type streamRule, Type generationRule, int deviceCount, int queueLength, int sources) {
        queueWaiting = new ArrayDeque<>(queueLength);
        devices = new ArrayList<>(deviceCount);
        this.sources = sources;

        for (int i = 0; i < deviceCount; i++) {
            devices.add(new Device());
        }
    }

    public void run(){

    }
}
