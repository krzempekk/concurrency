package lab4.zad1;

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

/*
    Producer 0 produced 2 units (2 units in buffer)
    Producer 0 produced 4 units (6 units in buffer)
    Consumer 0 consumed 5 units (1 units in buffer)
    Consumer 0 waits for 5 units (1 units in buffer)
    Consumer 1 waits, because firstConsumer has waiters
    Consumer 2 waits, because firstConsumer has waiters
    Producer 0 produced 5 units (6 units in buffer)
    Producer 0 waits with 5 units (6 units in buffer)
    Consumer 0 consumed 5 units (1 units in buffer)
    Consumer 0 waits for 2 units (1 units in buffer)
    Consumer 1 waits, because firstConsumer has waiters
    Producer 0 produced 5 units (6 units in buffer)
    Consumer 0 consumed 2 units (4 units in buffer)
    Consumer 0 consumed 4 units (0 units in buffer)
    Consumer 0 waits for 3 units (0 units in buffer)
    Producer 0 produced 2 units (2 units in buffer)
    Consumer 2 waits for 3 units (2 units in buffer)
    Consumer 1 waits, because firstConsumer has waiters
    Consumer 0 waits for 3 units (2 units in buffer)
    Producer 0 produced 3 units (5 units in buffer)
    Consumer 2 consumed 3 units (2 units in buffer)
    Consumer 2 waits, because firstConsumer has waiters
    Producer 0 produced 2 units (4 units in buffer)
    Producer 0 produced 1 units (5 units in buffer)
    Consumer 1 consumed 4 units (1 units in buffer)
    Consumer 1 consumed 1 units (0 units in buffer)
    Consumer 1 waits for 3 units (0 units in buffer)
    Consumer 0 waits for 3 units (0 units in buffer)
    Producer 0 produced 5 units (5 units in buffer)
    Producer 0 produced 3 units (8 units in buffer)
    Consumer 2 consumed 4 units (4 units in buffer)
    Consumer 2 waits for 5 units (4 units in buffer)
    Consumer 1 consumed 3 units (1 units in buffer)
    Consumer 0 waits for 3 units (1 units in buffer) - czeka w firstConsumer [FC: 0]
    Producer 0 produced 3 units (4 units in buffer) - C0 obudzony, czeka na lock [FC: 0!]
    Consumer 1 waits, because firstConsumer has waiters - czeka w restConsumers [FC: 0!; RC: 1]
    Consumer 2 waits for 5 units (4 units in buffer) - czeka w firstConsumer [FC: 0!, 2; RC: 1]
    Producer 0 produced 5 units (9 units in buffer) - C1 obudzony, czeka na lock [FC: 0!!, 2; RC: 1!]
    Consumer 0 consumed 3 units (6 units in buffer) - C0 dostał lock [FC: 2; RC: 1!]
    Consumer 0 waits, because firstConsumer has waiters - czeka w restConsumers [FC: 2; RC: 0, 1!]
    Producer 0 waits with 5 units (6 units in buffer) - czeka w firstProducer [FC: 2; RC: 0, 1!; FP: 0]
    Consumer 1 waits, because firstConsumer has waiters - dalej czeka w restConsumers [FC: 2; RC: 0, 1; FP: 0]
*/