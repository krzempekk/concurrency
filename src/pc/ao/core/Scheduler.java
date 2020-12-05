package pc.ao.core;

import pc.ao.method_request.MethodRequest;

public class Scheduler {
    ActivationQueue queue = new ActivationQueue();

    public void enqueue(MethodRequest request) {
        try {
            queue.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispatch() throws InterruptedException {
        MethodRequest request = queue.dequeue();
        request.execute();
    }
}