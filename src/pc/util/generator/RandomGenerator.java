package pc.util.generator;

public class RandomGenerator implements OrderGenerator {
    private int min;
    private int max;

    public RandomGenerator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getNextOrder() {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public boolean hasMoreOrders() {
        return true;
    }
}
