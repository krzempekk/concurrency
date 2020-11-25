package ao;

public class Scheduler {
    ActivationQueue queue;

    public Scheduler() {
        this.queue = new ActivationQueue();
    }

    public void enqueue(MethodRequest request) {
        queue.enqueue(request);
    }

    public void dispatch() {
        while (true) {
            MethodRequest request = queue.dequeue();
            if (request != null) {
                if (request.guard()) {
                    request.execute();
                } else {
                    queue.enqueue(request);
                }
            }
        }
    }
}
