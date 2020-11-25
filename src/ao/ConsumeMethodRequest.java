package ao;

public class ConsumeMethodRequest implements MethodRequest {
    private Future future;
    private Buffer buffer;
    private int unitsCount;

    public ConsumeMethodRequest(Future future, Buffer buffer, int unitsCount) {
        this.future = future;
        this.buffer = buffer;
        this.unitsCount = unitsCount;
    }

    public boolean guard() {
        return buffer.getCurrentUnitsCount() >= unitsCount;
    }

    public void execute() {
        int result = buffer.consume(unitsCount);
        future.set(result);
    }
}
