package ao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumers {
    private static int PRODUCER_COUNT = 2;
    private static int CONSUMER_COUNT = 2;
    private static int BUFFER_SIZE = 10;

    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static void main(String[] args) {
        BufferProxy buffer = new BufferProxy(BUFFER_SIZE);

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            int index = i;
            es.execute(() -> {
                while (true) {
                    int unitsCount = randomNumber(1, BUFFER_SIZE / 2);
                    Future future = buffer.produce(unitsCount);
                    while (!future.isAvailable()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Producer " + index + " produced " + unitsCount + " units, buffer units count: " + future.get());
                }
            });
        }
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            int index = i;
            es.execute(() -> {
                while (true) {
                    int unitsCount = randomNumber(1, BUFFER_SIZE / 2);
                    Future future = buffer.consume(unitsCount);
                    while (!future.isAvailable()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consumer " + index + " consumed " + unitsCount + " units, buffer units count: " + future.get());
                }
            });
        }

        es.execute(buffer::runScheduler);
    }

}