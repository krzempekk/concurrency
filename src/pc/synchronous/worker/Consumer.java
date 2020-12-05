package pc.synchronous.worker;

import pc.synchronous.Buffer;
import pc.util.generator.OrderGenerator;

public class Consumer extends Worker {
    public Consumer(int index, Buffer buffer, OrderGenerator generator, int secondaryTaskLength) {
        super(index, buffer, generator, secondaryTaskLength);
    }

    @Override
    protected void doPrimaryWork(int unitsCount) throws InterruptedException {
        buffer.consume(unitsCount, index);
    }
}
