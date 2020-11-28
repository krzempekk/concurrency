package pc.ao;

public interface MethodRequest {
    boolean guard();

    void execute();

    void markAsProcessed();

    boolean isProcessed();
}
