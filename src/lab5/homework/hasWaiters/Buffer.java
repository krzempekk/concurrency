package lab5.homework.hasWaiters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private List<Integer> units;
    private int maxUnits;
    ReentrantLock lock = new ReentrantLock();
    Condition firstProducer = lock.newCondition();
    Condition restProducers = lock.newCondition();
    Condition firstConsumer = lock.newCondition();
    Condition restConsumers = lock.newCondition();

    public Buffer(int size) {
        this.units = new ArrayList<>();
        this.maxUnits = size;
    }

    void produce(List<Integer> newUnits, int producerIndex) throws InterruptedException {
        lock.lock();
        try {
            while (lock.hasWaiters(firstProducer)) {
                System.out.println("Producer " + producerIndex + " waits, because firstProducer has waiters");
                restProducers.await();
            }

            while (this.units.size() > this.maxUnits - newUnits.size()) {
                System.out.println("Producer " + producerIndex + " waits with " + newUnits.size() + " units (" + units.size() + " units in buffer)");
                firstProducer.await();
            }

            this.units.addAll(newUnits);
            System.out.println("Producer " + producerIndex + " produced " + newUnits.size() + " units (" + units.size() + " units in buffer)");

            restProducers.signal();
            firstConsumer.signal();
        } finally {
            lock.unlock();
        }
    }

    List<Integer> consume(int unitsCount, int consumerIndex) throws InterruptedException {
        lock.lock();
        try {
            while (lock.hasWaiters(firstConsumer)) {
                System.out.println("Consumer " + consumerIndex + " waits, because firstConsumer has waiters");
                restConsumers.await();
            }

            while (this.units.size() < unitsCount) {
                System.out.println("Consumer " + consumerIndex + " waits for " + unitsCount + " units (" + units.size() + " units in buffer)");
                firstConsumer.await();
            }

            List<Integer> newUnits = new ArrayList<>();
            for (int i = 0; i < unitsCount; i++) {
                newUnits.add(this.units.remove(0));
            }
            System.out.println("Consumer " + consumerIndex + " consumed " + unitsCount + " units (" + units.size() + " units in buffer)");

            restConsumers.signal();
            firstProducer.signal();

            return newUnits;
        } finally {
            lock.unlock();
        }
    }
}

// lab5.homework.hasWaiters - wyciekanie zasobów