package pc.ao.core;

public class Buffer {
    private int currentUnitsCount;
    private int maxUnitsCount;
    private int primaryTaskLength;

    public Buffer(int maxUnitsCount, int primaryTaskLength) {
        this.maxUnitsCount = maxUnitsCount;
        this.currentUnitsCount = 0;
        this.primaryTaskLength = primaryTaskLength;
    }

    private void work() {
        try {
            Thread.sleep(primaryTaskLength);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentUnitsCount() {
        return currentUnitsCount;
    }

    public int getMaxUnitsCount() {
        return maxUnitsCount;
    }

    public int produce(int unitsCount) {
        currentUnitsCount += unitsCount;
        work();
        return currentUnitsCount;
    }

    public int consume(int unitsCount) {
        currentUnitsCount -= unitsCount;
        work();
        return currentUnitsCount;
    }
}
