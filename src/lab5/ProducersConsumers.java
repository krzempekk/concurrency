package lab5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducersConsumers {
    private static int PRODUCER_COUNT = 20;
    private static int CONSUMER_COUNT = 5;
    private static int BUFFER_SIZE = 15;

    public static int randomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static void main(String[] args) {
        TicketMachine ticketMachine = new TicketMachine(BUFFER_SIZE);

        Runnable producerRunnable = () -> {
            while (true) {
                int ticket = ticketMachine.takeProducerTicket();
                if (ticket != -1) {
                    double value = Math.random();
                    ticketMachine.buffer.produce(ticket, value);

                    System.out.println(ticketMachine.getTicketsCount() + " threads in buffer");

                    ticketMachine.returnProducerTicket(ticket);
                    System.out.println("Produced " + value);
                }
            }
        };

        Runnable consumerRunnable = () -> {
            while (true) {
                int ticket = ticketMachine.takeConsumerTicket();
                if (ticket != -1) {
                    double value = ticketMachine.buffer.consume(ticket);

                    System.out.println(ticketMachine.getTicketsCount() + " threads in buffer");

                    ticketMachine.returnConsumerTicket(ticket);
                    System.out.println("Consumed " + value);
                }
            }
        };

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < PRODUCER_COUNT; i++) es.execute(producerRunnable);
        for (int i = 0; i < CONSUMER_COUNT; i++) es.execute(consumerRunnable);
    }

}
// co daje ConcurrentHashmap, pokazac roznice