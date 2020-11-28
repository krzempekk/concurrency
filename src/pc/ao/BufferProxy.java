package pc.ao;

public class BufferProxy {
    Scheduler scheduler;
    private Buffer buffer;

    public BufferProxy(int maxUnitsCount) {
        this.scheduler = new Scheduler();
        this.buffer = new Buffer(maxUnitsCount);
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
