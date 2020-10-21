package lab3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumers {
    private static int PRODUCER_COUNT = 1;
    private static int CONSUMER_COUNT = 1;
    private static int BUFFER_SIZE = 3;

    public static void main(String[] args) {
        Buffer buffer = new Buffer(BUFFER_SIZE);

        Runnable producerRunnable = () -> {
            while (true) {
                try {
                    int unit = (int) (Math.random() * 1000);
                    buffer.produce(unit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumerRunnable = () -> {
            while (true) {
                try {
                    buffer.consume();
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
