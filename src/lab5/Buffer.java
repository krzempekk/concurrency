package lab5;

import java.util.HashMap;

public class Buffer {
    int maxSize;
    private final HashMap<Integer, Double> bufferMap = new HashMap<>();

    public void produce(int index, double value) {
        bufferMap.put(index, value);
    }

    public double consume(int index) {
        return bufferMap.get(index);
    }

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
    }
}

// porcje nie przepadaja
// w kazdym elemencie bufora moze byc watek
// nie trzeba zachowac kolejnosci produkcji - nie potrzeba tablicy do rezerwacji