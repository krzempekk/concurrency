package ao;

import java.util.ArrayList;
import java.util.List;

public class ActivationQueue {
    private List<MethodRequest> queue = new ArrayList<>();

    public synchronized void enqueue(MethodRequest request) {
        queue.add(request);
    }

    public synchronized MethodRequest dequeue() {
        return queue.size() > 0 ? queue.remove(0) : null;
    }
}
