package pc;

abstract public class ProducersConsumers {
    protected String PRODUCER_FILE_PREFIX;
    protected String CONSUMER_FILE_PREFIX;
    protected int PRODUCER_COUNT;
    protected int CONSUMER_COUNT;
    protected int BUFFER_SIZE;
    protected int MAX_UNITS_REQUEST;
    protected int TIME_QUANTUM;
    protected int PRIMARY_TASK_LENGTH;
    protected int SECONDARY_TASK_LENGTH;

    public void initialize(String PRODUCER_FILE_PREFIX, String CONSUMER_FILE_PREFIX, int PRODUCER_COUNT, int CONSUMER_COUNT, int BUFFER_SIZE, int MAX_UNITS_REQUEST, int TIME_QUANTUM, int PRIMARY_TASK_LENGTH, int SECONDARY_TASK_LENGTH) {
        this.PRODUCER_FILE_PREFIX = PRODUCER_FILE_PREFIX;
        this.CONSUMER_FILE_PREFIX = CONSUMER_FILE_PREFIX;
        this.PRODUCER_COUNT = PRODUCER_COUNT;
        this.CONSUMER_COUNT = CONSUMER_COUNT;
        this.BUFFER_SIZE = BUFFER_SIZE;
        this.MAX_UNITS_REQUEST = MAX_UNITS_REQUEST;
        this.TIME_QUANTUM = TIME_QUANTUM;
        this.PRIMARY_TASK_LENGTH = PRIMARY_TASK_LENGTH;
        this.SECONDARY_TASK_LENGTH = SECONDARY_TASK_LENGTH;
    }

    abstract public void run();
}
