package pc.ao.worker;

import pc.ao.core.BufferProxy;
import pc.ao.core.Future;
import pc.util.generator.OrderGenerator;

public abstract class Worker implements Runnable {
    protected final int index;
    protected final BufferProxy buffer;
    protected final OrderGenerator generator;
    protected final int secondaryTaskLength;
    protected final int taskQuantum;

    protected Worker(int index, BufferProxy buffer, OrderGenerator generator, int secondaryTaskLength, int taskQuantum) {
        this.index = index;
        this.buffer = buffer;
        this.generator = generator;
        this.secondaryTaskLength = secondaryTaskLength;
        this.taskQuantum = taskQuantum;
    }

    @Override
    public void run() {
        int secondaryTaskLeftover = 0;
        while (generator.hasMoreOrders()) {
            int unitsCount = generator.getNextOrder();
            Future future = doPrimaryWork(unitsCount);
            int secondaryTaskLengthLeft = secondaryTaskLength - secondaryTaskLeftover;
            int taskQuantum = 1;
            while (!future.isAvailable()) {
                doSecondaryWork(taskQuantum);
                secondaryTaskLengthLeft -= taskQuantum;
            }
            if (secondaryTaskLengthLeft > 0) doSecondaryWork(secondaryTaskLengthLeft);
            else secondaryTaskLeftover = -secondaryTaskLengthLeft;
        }
    }

    protected abstract Future doPrimaryWork(int unitsCount);

    protected void doSecondaryWork(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
