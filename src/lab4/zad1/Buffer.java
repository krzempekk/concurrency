package lab4.zad1;

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

    void produce(List<Integer> newUnits) throws InterruptedException {
        lock.lock();
        try {
            while (lock.hasWaiters(firstProducer)) restProducers.await();

            while (this.units.size() > this.maxUnits - newUnits.size()) {
                System.out.println("Produces waits with " + newUnits.size() + " units");
                firstProducer.await();
            }

            this.units.addAll(newUnits);
            System.out.println("Produced " + newUnits.size() + " units");

            restProducers.signal();
            firstConsumer.signal();
        } finally {
            lock.unlock();
        }
    }

    List<Integer> consume(int unitsCount) throws InterruptedException {
        lock.lock();
        try {
            while (lock.hasWaiters(firstConsumer)) restConsumers.await();

            while (this.units.size() < unitsCount) {
                System.out.println("Consumer waits for " + unitsCount + " units");
                firstConsumer.await();
            }

            List<Integer> newUnits = new ArrayList<>();
            for (int i = 0; i < unitsCount; i++) {
                newUnits.add(this.units.remove(0));
            }
            System.out.println("Consumed " + unitsCount + " units");

            restConsumers.signal();
            firstProducer.signal();

            return newUnits;
        } finally {
            lock.unlock();
        }
    }
}