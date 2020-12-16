package lab9.zad1;

import org.jcsp.lang.*;

public class Consumer implements CSProcess {
    private final One2OneChannelInt channel;

    public Consumer(final One2OneChannelInt in) {
        this.channel = in;
    }

    public void run() {
        while (true) {
            int item = channel.in().read();
            System.out.println(item);
        }
    }
}