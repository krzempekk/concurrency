package lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private List<Integer> units;
    private int maxUnits;
    Lock lock = new ReentrantLock();
    Condition isEmpty = lock.newCondition();
    Condition isFull = lock.newCondition();

    public Buffer(int size) {
        this.units = new ArrayList<>();
        this.maxUnits = size;
    }

    void produce(List<Integer> newUnits) throws InterruptedException {
        lock.lock();
        try {
            while (this.units.size() > this.maxUnits - newUnits.size()) {
                System.out.println("Produces waits with " + newUnits.size() + " units");
                isFull.await();
            }
            this.units.addAll(newUnits);
            System.out.println("Produced " + newUnits.size() + " units");
            isEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    List<Integer> consume(int unitsCount) throws InterruptedException {
        lock.lock();
        try {
            while (this.units.size() < unitsCount) {
                System.out.println("Consumer waits for " + unitsCount + " units");
                isEmpty.await();
            }
            List<Integer> newUnits = new ArrayList<>();
            for (int i = 0; i < unitsCount; i++) {
                newUnits.add(this.units.remove(0));
            }
            System.out.println("Consumed " + unitsCount + " units");
            isFull.signal();
            return newUnits;
        } finally {
            lock.unlock();
        }
    }
}