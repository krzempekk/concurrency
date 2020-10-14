package lab2;

public class Semaphore {
    private int value;

    public Semaphore(int value) {
        this.value = value;
    }

    synchronized void acquire() throws InterruptedException {
        while (value <= 0) wait();
        value--;
    }

    synchronized void release() {
        value++;
        notify();
    }

}
