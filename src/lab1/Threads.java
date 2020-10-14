package lab1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Threads {
    private static final int THREAD_COUNT = 10000;
    private static final int MIN_SLEEP = 10;
    private static final int MAX_SLEEP = 500;

    public static long randomSleepValue() {
        return (long) (Math.random() * (MAX_SLEEP - MIN_SLEEP + 1) + MIN_SLEEP);
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Runnable incrementRunnable = () -> {
            try {
                Thread.sleep(randomSleepValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.increment(); };
        Runnable decrementRunnable = () -> {
            try {
                Thread.sleep(randomSleepValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.decrement(); };

        ExecutorService es = Executors.newCachedThreadPool();
        for(int i = 0; i < THREAD_COUNT / 2; i++) {
            es.execute(incrementRunnable);
            es.execute(decrementRunnable);
        }
        es.shutdown();

        boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(finished ? counter.getAmount() : "Timeout");
    }
}

// definicja teoretyczna i praktyczna semaforów