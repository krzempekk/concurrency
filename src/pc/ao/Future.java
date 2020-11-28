package pc.ao;

public class Future {
    private int result;
    private boolean isFinished = false;

    public boolean isAvailable() {
        return isFinished;
    }

    public void set(int result) {
        this.result = result;
        this.isFinished = true;
    }

    public int get() {
        return result;
    }
}
