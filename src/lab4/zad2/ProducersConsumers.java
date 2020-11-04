package lab4.zad2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumers {
    private static int PRODUCER_COUNT = 5;
    private static int CONSUMER_COUNT = 1;
    private static int BUFFER_SIZE = 5;

    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer(BUFFER_SIZE);

        Runnable producerRunnable = () -> {
            while (true) {
                try {
                    int index = buffer.startProducing();
                    if (index < 0) continue;
                    Thread.sleep(randomNumber(0, 500));
                    buffer.endProducing(index);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumerRunnable = () -> {
            while (true) {
                try {
                    int index = buffer.startConsuming();
                    Thread.sleep(randomNumber(0, 500));
                    buffer.endConsuming(index);
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
