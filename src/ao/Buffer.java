package ao;

public class Buffer {
    private int currentUnitsCount;
    private int maxUnitsCount;

    public Buffer(int maxUnitsCount) {
        this.maxUnitsCount = maxUnitsCount;
        this.currentUnitsCount = 0;
    }

    public int getCurrentUnitsCount() {
        return currentUnitsCount;
    }

    public int getMaxUnitsCount() {
        return maxUnitsCount;
    }

    int produce(int unitsCount) {
        currentUnitsCount += unitsCount;
        return currentUnitsCount;
    }

    int consume(int unitsCount) {
        currentUnitsCount -= unitsCount;
        return currentUnitsCount;
    }
}
