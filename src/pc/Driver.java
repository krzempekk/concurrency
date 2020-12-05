package pc;

import pc.ao.ProducersConsumersAO;
import pc.synchronous.ProducersConsumersSynchronous;

public class Driver {
    public static void main(String[] args) {
        try {
            boolean synchronous = args[0].equals("synchronous");
            String producerFilePrefix = args[1];
            String consumerFilePrefix = args[2];
            int producerCount = Integer.parseInt(args[3]);
            int consumerCount = Integer.parseInt(args[4]);
            int bufferSize = Integer.parseInt(args[5]);
            int maxUnitsRequest = Integer.parseInt(args[6]);
            int timeQuantum = Integer.parseInt(args[7]);
            int primaryTaskLength = Integer.parseInt(args[8]);
            int secondaryTaskLength = Integer.parseInt(args[9]);

            ProducersConsumers pc = synchronous ? new ProducersConsumersSynchronous() : new ProducersConsumersAO();
            pc.initialize(
                producerFilePrefix,
                consumerFilePrefix,
                producerCount,
                consumerCount,
                bufferSize,
                maxUnitsRequest,
                timeQuantum,
                primaryTaskLength,
                secondaryTaskLength
            );

            pc.run();
        } catch (Exception e) {
            System.out.println("Cannot parse input arguments, quitting");
        }
    }
}