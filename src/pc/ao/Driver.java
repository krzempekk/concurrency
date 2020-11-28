package pc.ao;

public class Driver {
    public static void main(String[] args) {
        try {
            int producerCount = Integer.parseInt(args[0]);
            int consumerCount = Integer.parseInt(args[1]);
            int bufferSize = Integer.parseInt(args[2]);
            int maxUnitsRequest = Integer.parseInt(args[3]);
            String producerFilePrefix = args[4];
            String consumerFilePrefix = args[5];
            ProducersConsumers pc = new ProducersConsumers(producerCount,
                                                           consumerCount,
                                                           bufferSize,
                                                           maxUnitsRequest,
                                                           producerFilePrefix,
                                                           consumerFilePrefix
            );
            pc.run();
        } catch (Exception e) {
            System.out.println("Cannot parse input arguments, quitting");
        }
    }
}