package pc.ao;

import pc.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumers {
    private final int PRODUCER_COUNT;
    private final int CONSUMER_COUNT;
    private final int BUFFER_SIZE;
    private final int MAX_UNITS_REQUEST;
    private final String PRODUCER_FILE_PREFIX;
    private final String CONSUMER_FILE_PREFIX;
    private int finishedCount;

    public ProducersConsumers(int PRODUCER_COUNT, int CONSUMER_COUNT, int BUFFER_SIZE, int MAX_UNITS_REQUEST, String PRODUCER_FILE_PREFIX, String CONSUMER_FILE_PREFIX) {
        this.PRODUCER_COUNT = PRODUCER_COUNT;
        this.CONSUMER_COUNT = CONSUMER_COUNT;
        this.BUFFER_SIZE = BUFFER_SIZE;
        this.MAX_UNITS_REQUEST = MAX_UNITS_REQUEST;
        this.PRODUCER_FILE_PREFIX = PRODUCER_FILE_PREFIX;
        this.CONSUMER_FILE_PREFIX = CONSUMER_FILE_PREFIX;
        this.finishedCount = PRODUCER_COUNT + CONSUMER_COUNT;
    }

    public int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private void doThings(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized int decrementFinishedCount() {
        this.finishedCount--;
        return this.finishedCount;
    }

    public void run() {
        BufferProxy buffer = new BufferProxy(BUFFER_SIZE);

        ExecutorService prodCons = Executors.newCachedThreadPool();
        ExecutorService scheduler = Executors.newCachedThreadPool();

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            int index = i;
            prodCons.execute(() -> {
                Random random = new Random(PRODUCER_FILE_PREFIX + index);
                while (random.hasMoreNumbers()) {
                    int unitsCount = random.getRandomNumber();
                    Future future = buffer.produce(unitsCount);
                    while (!future.isAvailable()) {
                        doThings(1);
                    }
                    //                    doThings(100);
                }
                int value = this.decrementFinishedCount();
                if (value == 0) {
                    scheduler.shutdownNow();
                }
            });
        }
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            int index = i;
            prodCons.execute(() -> {
                Random random = new Random(CONSUMER_FILE_PREFIX + index);
                while (random.hasMoreNumbers()) {
                    int unitsCount = random.getRandomNumber();
                    Future future = buffer.consume(unitsCount);
                    while (!future.isAvailable()) {
                        doThings(1);
                    }
                    //                    doThings(500);
                }
                int value = this.decrementFinishedCount();
                if (value == 0) {
                    scheduler.shutdownNow();
                }
            });
        }

        scheduler.execute(() -> {
            while (true) {
                try {
                    buffer.scheduler.dispatch();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        prodCons.shutdown();
        scheduler.shutdown();
    }

}