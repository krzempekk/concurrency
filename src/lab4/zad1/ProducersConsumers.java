package lab4.zad1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumers {
    private static int PRODUCER_COUNT = 5;
    private static int CONSUMER_COUNT = 5;
    private static int BUFFER_SIZE = 10;

    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer(BUFFER_SIZE);

        Runnable producerRunnable = () -> {
            while (true) {
                try {
                    int unitsCount = randomNumber(1, BUFFER_SIZE / 2);
                    List<Integer> units = new ArrayList<>();
                    for (int i = 0; i < unitsCount; i++) units.add(randomNumber(0, 1000));
                    buffer.produce(units);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumerRunnable = () -> {
            while (true) {
                try {
                    buffer.consume(randomNumber(1, BUFFER_SIZE / 2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < PRODUCER_COUNT; i++) es.execute(producerRunnable);
        for (int i = 0; i < CONSUMER_COUNT; i++) es.execute(consumerRunnable);
    }

}
