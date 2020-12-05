package pc.synchronous.worker;

import pc.synchronous.Buffer;
import pc.util.generator.OrderGenerator;

public abstract class Worker implements Runnable {
    protected final int index;
    protected final Buffer buffer;
    protected final OrderGenerator generator;
    protected final int secondaryTaskLength;

    protected Worker(int index, Buffer buffer, OrderGenerator generator, int secondaryTaskLength) {
        this.index = index;
        this.buffer = buffer;
        this.generator = generator;
        this.secondaryTaskLength = secondaryTaskLength;
    }

    @Override
    public void run() {
        while (generator.hasMoreOrders()) {
            try {
                int unitsCount = generator.getNextOrder();
                doPrimaryWork(unitsCount);
                doSecondaryWork(this.secondaryTaskLength);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void doPrimaryWork(int unitsCount) throws InterruptedException;

    protected void doSecondaryWork(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
