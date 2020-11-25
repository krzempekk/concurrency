package ao;

public class BufferProxy {
    private Scheduler scheduler;
    private Buffer buffer;

    public BufferProxy(int maxUnitsCount) {
        this.scheduler = new Scheduler();
        this.buffer = new Buffer(maxUnitsCount);
    }

    public void runScheduler() {
        this.scheduler.dispatch();
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
