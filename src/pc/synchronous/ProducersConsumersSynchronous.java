package pc.synchronous;

import pc.ProducersConsumers;
import pc.synchronous.worker.Consumer;
import pc.synchronous.worker.Producer;
import pc.util.generator.OrderGenerator;
import pc.util.generator.TestGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumersSynchronous extends ProducersConsumers {
    public void run() {
        Buffer buffer = new Buffer(BUFFER_SIZE, PRIMARY_TASK_LENGTH);

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            // OrderGenerator generator = new RandomGenerator(1, MAX_UNITS_REQUEST);
            OrderGenerator generator = new TestGenerator(PRODUCER_FILE_PREFIX + i);
            es.execute(new Producer(i, buffer, generator, SECONDARY_TASK_LENGTH));
        }
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            // OrderGenerator generator = new RandomGenerator(1, MAX_UNITS_REQUEST);
            OrderGenerator generator = new TestGenerator(CONSUMER_FILE_PREFIX + i);
            es.execute(new Consumer(i, buffer, generator, SECONDARY_TASK_LENGTH));
        }

        es.shutdown();
    }

}