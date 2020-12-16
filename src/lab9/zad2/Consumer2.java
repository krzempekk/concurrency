package lab9.zad2;

import org.jcsp.lang.*;

/**
 * Consumer class: reads ints from input channel, displays them,
 * then
 * terminates when a negative value is read.
 */
public class Consumer2 implements CSProcess {
    private One2OneChannelInt channelIn;
    private One2OneChannelInt channelReq;

    public Consumer2(final One2OneChannelInt req, final One2OneChannelInt in) {
        this.channelReq = req;
        this.channelIn = in;
    }

    public void run() {
        int item;
        ChannelOutputInt reqOut = channelReq.out();
        ChannelInputInt inputIn = channelIn.in();
        while (true) {
            reqOut.write(0);
            item = inputIn.read();
            if (item < 0) break;
            System.out.println(item);
        }
        System.out.println("Consumer ended.");
    }
}