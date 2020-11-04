package lab4.zad2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private enum State {
        EMPTY, BUSY, FULL
    }

    private State[] units;
    private int maxUnits;
    private int produceIndex, consumeIndex;
    ReentrantLock lock = new ReentrantLock();
    Condition blocked = lock.newCondition();

    public Buffer(int size) {
        this.units = new State[size];
        for (int i = 0; i < size; i++) units[i] = State.EMPTY;
        this.maxUnits = size;
        produceIndex = 0;
        consumeIndex = 0;
    }

    int startProducing() {
        lock.lock();
        try {
            if (units[produceIndex] != State.EMPTY) return -1;

            int index = produceIndex;
            units[index] = State.BUSY;
            produceIndex = (produceIndex + 1) % maxUnits;
            System.out.println(index + ": started producing");
            return index;
        } finally {
            lock.unlock();
        }
    }

    void endProducing(int index) {
        lock.lock();
        try {
            units[index] = State.FULL;
            if (index == consumeIndex) blocked.signal();
            System.out.println(index + ": ended producing");
        } finally {
            lock.unlock();
        }
    }

    int startConsuming() throws InterruptedException {
        lock.lock();
        try {
            if (units[consumeIndex] != State.FULL) blocked.await();
            int index = consumeIndex;
            consumeIndex = (consumeIndex + 1) % maxUnits;
            System.out.println(index + ": started consuming");
            return index;
        } finally {
            lock.unlock();
        }
    }

    void endConsuming(int index) {
        lock.lock();
        try {
            units[index] = State.EMPTY;
            System.out.println(index + ": ended consuming");
        } finally {
            lock.unlock();
        }
    }
}