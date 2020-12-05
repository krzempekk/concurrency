package pc.ao.method_request;

public interface MethodRequest {
    boolean guard();

    void execute();

    void markAsProcessed();

    boolean isProcessed();
}
