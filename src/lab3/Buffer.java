package lab3;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
    private List<Integer> units;
    private int maxUnits;

    public Buffer(int size) {
        this.units = new ArrayList<>();
        this.maxUnits = size;
    }

    synchronized void produce(int unit) throws InterruptedException {
        while (this.units.size() >= this.maxUnits) {
            System.out.println("Produces waits with value " + unit);
            wait();
        }
        this.units.add(unit);
        System.out.println("Produced unit of value " + unit);
        notify();
    }

    synchronized int consume() throws InterruptedException {
        while (this.units.isEmpty()) {
            System.out.println("Consumer waits");
            wait();
        }
        int unit = this.units.remove(0);
        System.out.println("Consumed unit of value " + unit);
        notify();
        return unit;
    }
}
