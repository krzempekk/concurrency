package pc.ao.method_request;

import pc.ao.core.Buffer;
import pc.ao.core.Future;

public class ConsumeMethodRequest implements MethodRequest {
    private Future future;
    private Buffer buffer;
    private int unitsCount;
    private boolean processed;

    public ConsumeMethodRequest(Future future, Buffer buffer, int unitsCount) {
        this.future = future;
        this.buffer = buffer;
        this.unitsCount = unitsCount;
        this.processed = false;
    }

    public boolean guard() {
        return buffer.getCurrentUnitsCount() >= unitsCount;
    }

    public void execute() {
        int result = buffer.consume(unitsCount);
        //        System.out.println("Consumed " + unitsCount + " units, buffor units count: " + result);
        future.set(result);
    }

    @Override
    public void markAsProcessed() {
        this.processed = true;
    }

    @Override
    public boolean isProcessed() {
        return this.processed;
    }
}
