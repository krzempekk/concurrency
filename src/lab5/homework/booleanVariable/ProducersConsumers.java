package lab5.homework.booleanVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumers {
    private static int PRODUCER_COUNT = 1;
    private static int CONSUMER_COUNT = 4;
    private static int BUFFER_SIZE = 10;

    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer(BUFFER_SIZE);

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            int index = i;
            es.execute(() -> {
                while (true) {
                    try {
                        int unitsCount = randomNumber(1, BUFFER_SIZE / 2);
                        List<Integer> units = new ArrayList<>();
                        for (int j = 0; j < unitsCount; j++) units.add(randomNumber(0, 1000));
                        buffer.produce(units, index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            int index = i;
            es.execute(() -> {
                while (true) {
                    try {
                        int unitsCount = randomNumber(1, BUFFER_SIZE / 2);
                        buffer.consume(unitsCount, index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}