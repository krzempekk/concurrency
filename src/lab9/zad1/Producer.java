package lab9.zad1;

import org.jcsp.lang.*;

public class Producer implements CSProcess {
    private final One2OneChannelInt channel;

    public Producer(final One2OneChannelInt out) {
        this.channel = out;
    }

    public void run() {
        while (true) {
            int item = (int) (Math.random() * 100) + 1;
            channel.out().write(item);
        }
    }
}