package ao;

public class ProduceMethodRequest implements MethodRequest {
    private Future future;
    private Buffer buffer;
    private int unitsCount;

    public ProduceMethodRequest(Future future, Buffer buffer, int unitsCount) {
        this.future = future;
        this.buffer = buffer;
        this.unitsCount = unitsCount;
    }

    public boolean guard() {
        return buffer.getCurrentUnitsCount() + unitsCount <= buffer.getMaxUnitsCount();
    }

    public void execute() {
        int result = buffer.produce(unitsCount);
        future.set(result);
    }

}
