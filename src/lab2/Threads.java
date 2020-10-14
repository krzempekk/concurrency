package lab2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Threads {
    private static final int THREAD_COUNT = 10000;
    private static final int MIN_SLEEP = 10;
    private static final int MAX_SLEEP = 500;

    private static long randomSleepValue() {
        return (long) (Math.random() * (Threads.MAX_SLEEP - Threads.MIN_SLEEP + 1) + Threads.MIN_SLEEP);
    }

    private static Runnable getRunnable(Counter counter, BinarySemaphore semaphore, boolean isIncremental) {
        return () -> {
            try {
                Thread.sleep(Threads.randomSleepValue());
                semaphore.acquire();
                if (isIncremental) counter.increment();
                else counter.decrement();
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        BinarySemaphore semaphore = new BinarySemaphore(true);

        Runnable incrementRunnable = Threads.getRunnable(counter, semaphore, true);
        Runnable decrementRunnable = Threads.getRunnable(counter, semaphore, false);

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < Threads.THREAD_COUNT / 2; i++) {
            es.execute(incrementRunnable);
            es.execute(decrementRunnable);
        }
        es.shutdown();

        boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(finished ? counter.getAmount() : "Timeout");
    }
}

// definicja teoretyczna i praktyczna semaforów