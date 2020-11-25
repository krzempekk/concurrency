package lab5;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketMachine {
    public final Queue<Integer> emptyCells = new LinkedList<>(), fullCells = new LinkedList<>();
    public final Buffer buffer;
    private final Lock producerLock = new ReentrantLock(), consumerLock = new ReentrantLock();
    private final Condition producerCondition = producerLock.newCondition();
    private final Condition consumerCondition = consumerLock.newCondition();

    private TicketMachine(Buffer buffer) {
        this.buffer = buffer;
        for (int i = 0; i < buffer.maxSize; i++) {
            emptyCells.add(i);
        }
    }

    public TicketMachine(int size) {
        this(new Buffer(size));
    }

    public int getTicketsCount() {
        return buffer.maxSize - emptyCells.size() - fullCells.size();
    }

    public int takeProducerTicket() {
        return takeTicket(producerLock, emptyCells, producerCondition);
    }

    public void returnProducerTicket(int ticket) {
        consumerLock.lock();
        fullCells.add(ticket);
        consumerCondition.signal();
        consumerLock.unlock();
    }

    public int takeConsumerTicket() {
        return takeTicket(consumerLock, fullCells, consumerCondition);
    }

    public void returnConsumerTicket(int ticket) {
        producerLock.lock();
        emptyCells.add(ticket);
        producerCondition.signal();
        producerLock.unlock();
    }

    private int takeTicket(Lock lock, Queue<Integer> queue, Condition condition) {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return -1;
    }
}
