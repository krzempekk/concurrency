package pc.synchronous;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private int currentUnits;
    private int maxUnits;
    private int primaryTaskLength;
    ReentrantLock lock = new ReentrantLock();
    Condition firstProducer = lock.newCondition();
    Condition restProducers = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition restConsumers = lock.newCondition();

    boolean isFirstProducer = false;
    boolean isFirstConsumer = false;

    public Buffer(int size, int primaryTaskLength) {
        this.currentUnits = 0;
        this.maxUnits = size;
        this.primaryTaskLength = primaryTaskLength;
    }

    private void work() {
        try {
            Thread.sleep(primaryTaskLength);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void produce(int unitsCount, int producerIndex) throws InterruptedException {
        lock.lock();
        try {
            while (isFirstProducer) restProducers.await();
            isFirstProducer = true;

            while (this.currentUnits > this.maxUnits - unitsCount) {
                firstProducer.await();
            }

            this.currentUnits += unitsCount;
            //            System.out.println("Producer " + producerIndex + " produced " + unitsCount + " units (" + this.currentUnits + " units in buffer)");

            work();

            restProducers.signal();
            firstConsumer.signal();

            isFirstProducer = false;
        } finally {
            lock.unlock();
        }
    }

    public void consume(int unitsCount, int consumerIndex) throws InterruptedException {
        lock.lock();
        try {
            while (isFirstConsumer) restConsumers.await();
            isFirstConsumer = true;

            while (this.currentUnits < unitsCount) {
                firstConsumer.await();
            }

            this.currentUnits -= unitsCount;
            //            System.out.println("Consumer " + consumerIndex + " consumed " + unitsCount + " units (" + this.currentUnits + " units in buffer)");

            work();

            restConsumers.signal();
            firstProducer.signal();

            isFirstConsumer = false;
        } finally {
            lock.unlock();
        }
    }
}