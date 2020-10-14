package lab1;

public class Counter {
    private int amount = 0;

    public void increment() {
        this.amount++;
    }

    public void decrement() {
        this.amount--;
    }

    public int getAmount() {
        return this.amount;
    }
}
