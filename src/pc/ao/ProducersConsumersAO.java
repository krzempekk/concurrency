package pc.ao;

import pc.ProducersConsumers;
import pc.ao.core.BufferProxy;
import pc.ao.worker.Consumer;
import pc.ao.worker.Producer;
import pc.util.generator.OrderGenerator;
import pc.util.generator.RandomGenerator;
import pc.util.generator.TestGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProducersConsumersAO extends ProducersConsumers {
    public void run() {
        BufferProxy buffer = new BufferProxy(BUFFER_SIZE, PRIMARY_TASK_LENGTH);

        ExecutorService prodCons = Executors.newCachedThreadPool();
        ExecutorService scheduler = Executors.newCachedThreadPool();

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            // OrderGenerator generator = new RandomGenerator(1, MAX_UNITS_REQUEST);
            OrderGenerator generator = new TestGenerator(PRODUCER_FILE_PREFIX + i);
            prodCons.execute(new Producer(i, buffer, generator, SECONDARY_TASK_LENGTH, TIME_QUANTUM));
        }

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            // OrderGenerator generator = new RandomGenerator(1, MAX_UNITS_REQUEST);
            OrderGenerator generator = new TestGenerator(CONSUMER_FILE_PREFIX + i);
            prodCons.execute(new Consumer(i, buffer, generator, SECONDARY_TASK_LENGTH, TIME_QUANTUM));
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

        try {
            prodCons.shutdown();
            if (!prodCons.awaitTermination(1, TimeUnit.MINUTES)) {
                prodCons.shutdownNow();
            }
            scheduler.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}