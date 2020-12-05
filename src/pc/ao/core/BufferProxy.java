package pc.ao.core;

import pc.ao.method_request.ConsumeMethodRequest;
import pc.ao.method_request.ProduceMethodRequest;

public class BufferProxy {
    public Scheduler scheduler;
    private Buffer buffer;

    public BufferProxy(int maxUnitsCount, int primaryTaskLength) {
        this.scheduler = new Scheduler();
        this.buffer = new Buffer(maxUnitsCount, primaryTaskLength);
    }

    public Future produce(int unitsCount) {
        Future future = new Future();
        ProduceMethodRequest request = new ProduceMethodRequest(future, buffer, unitsCount);
        scheduler.enqueue(request);
        return future;
    }

    public Future consume(int unitsCount) {
        Future future = new Future();
        ConsumeMethodRequest request = new ConsumeMethodRequest(future, buffer, unitsCount);
        scheduler.enqueue(request);
        return future;
    }
}
