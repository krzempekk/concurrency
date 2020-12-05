package pc.ao.worker;

import pc.ao.core.BufferProxy;
import pc.ao.core.Future;
import pc.util.generator.OrderGenerator;

public class Consumer extends Worker {
    public Consumer(int index, BufferProxy buffer, OrderGenerator generator, int secondaryTaskLength, int taskQuantum) {
        super(index, buffer, generator, secondaryTaskLength, taskQuantum);
    }

    @Override
    protected Future doPrimaryWork(int unitsCount) {
        return buffer.consume(unitsCount);
    }
}
