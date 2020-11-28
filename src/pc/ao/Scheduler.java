package pc.ao;

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

/*
    ActivationQueue producerQueue;
    ActivationQueue consumerQueue;

    public Scheduler() {
        this.producerQueue = new ActivationQueue();
        this.consumerQueue = new ActivationQueue();
    }

    public void enqueue(MethodRequest request) {
        if (request instanceof ConsumeMethodRequest) consumerQueue.enqueue(request);
        else producerQueue.enqueue(request);
    }

    public void dispatch() {
        ActivationQueue currentQueue = producerQueue;
        while (true) {
            MethodRequest request = currentQueue.head();
            if (request != null && request.guard()) {
                currentQueue.dequeue();
                request.execute();
            } else {
                currentQueue = currentQueue == producerQueue ? consumerQueue : producerQueue;
            }
        }
    }
 */

/*
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


 */