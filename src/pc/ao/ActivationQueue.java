package pc.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActivationQueue {
    private List<MethodRequest> queue = new ArrayList<>();
    private List<MethodRequest> producerQueue = new ArrayList<>();
    private List<MethodRequest> consumerQueue = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    private Condition schedulerCondition = lock.newCondition();

    public void enqueue(MethodRequest request) {
        lock.lock();
        try {
            queue.add(request);
            if (request instanceof ConsumeMethodRequest) consumerQueue.add(request);
            else producerQueue.add(request);
            schedulerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    private MethodRequest tryDequeue() {
        // check main queue
        MethodRequest nextRequest = null;
        do {
            if (nextRequest != null) queue.remove(0);
            if (queue.size() == 0) return null;
            nextRequest = queue.get(0);
        } while (nextRequest.isProcessed());

        // check active queue
        List<MethodRequest> activeQueue = nextRequest instanceof ConsumeMethodRequest ? consumerQueue : producerQueue;
        if (nextRequest.guard()) {
            queue.remove(0);
            activeQueue.remove(0);
            return nextRequest;
        }

        // check other queue and mark as processed if needed
        List<MethodRequest> otherQueue = activeQueue == consumerQueue ? producerQueue : consumerQueue;
        if (otherQueue.size() == 0) return null;
        nextRequest = otherQueue.get(0);
        if (nextRequest.guard()) {
            nextRequest.markAsProcessed();
            return otherQueue.remove(0);
        } else {
            return null;
        }
    }

    public MethodRequest dequeue() throws InterruptedException {
        lock.lock();
        MethodRequest request;
        try {
            request = tryDequeue();
            while (request == null) {
                schedulerCondition.await();
                request = tryDequeue();
            }
        } finally {
            lock.unlock();
        }
        return request;
    }
}