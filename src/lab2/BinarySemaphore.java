package lab2;

class BinarySemaphore {
    private boolean value;

    BinarySemaphore(boolean value) {
        this.value = value;
    }

    synchronized void acquire() throws InterruptedException {
        while (!value) wait();
        value = false;
    }

    synchronized void release() {
        value = true;
        notify();
    }
}
