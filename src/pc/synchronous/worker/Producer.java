package pc.synchronous.worker;

import pc.synchronous.Buffer;
import pc.util.generator.OrderGenerator;

public class Producer extends Worker {
    public Producer(int index, Buffer buffer, OrderGenerator generator, int secondaryTaskLength) {
        super(index, buffer, generator, secondaryTaskLength);
    }

    @Override
    protected void doPrimaryWork(int unitsCount) throws InterruptedException {
        buffer.produce(unitsCount, index);
    }
}
