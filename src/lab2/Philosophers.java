package lab2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Philosophers {
    private static final int PHILOSOPHERS_COUNT = 5;
    private static final Philosopher[] philosophers = new Philosopher[PHILOSOPHERS_COUNT];
    private static final BinarySemaphore[] forks = new BinarySemaphore[PHILOSOPHERS_COUNT];
    private static final Semaphore waiter = new Semaphore(PHILOSOPHERS_COUNT - 1);

    static void takeFork(int index) throws InterruptedException {
        forks[index].acquire();
    }

    static void returnFork(int index) {
        forks[index].release();
    }

    public static void main(String[] args) {
        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(i);
            forks[i] = new BinarySemaphore(true);
        }

        ExecutorService es = Executors.newCachedThreadPool();
        for (Philosopher philosopher : philosophers) es.execute(philosopher);
    }

    static class Philosopher implements Runnable {
        private final int index;

        Philosopher(int index) {
            this.index = index;
        }

        void printInfo(String message) {
            System.out.println("Philosopher " + index + ": " + message);
        }

        @Override
        public void run() {
            while (true) {
                printInfo("thinking");
                int leftForkIndex = index;
                int rightForkIndex = (index + 1) % Philosophers.PHILOSOPHERS_COUNT;
                try {
                    waiter.acquire();
                    Philosophers.takeFork(leftForkIndex);
                    printInfo("taken left fork");
                    Philosophers.takeFork(rightForkIndex);
                    printInfo("taken right fork");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printInfo("eating");
                Philosophers.returnFork(leftForkIndex);
                Philosophers.returnFork(rightForkIndex);
                waiter.release();
                printInfo("returned both forks");
            }
        }
    }
}