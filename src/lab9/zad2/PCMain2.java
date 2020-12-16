package lab9.zad2;

import org.jcsp.lang.*;

/**
 * Main program class for Producer/Consumer example.
 * Sets up channels, creates processes then
 * executes them in parallel, using JCSP.
 */
public final class PCMain2 {
    public static void main(String[] args) {
        new PCMain2();
    }

    public PCMain2() {
        final One2OneChannelInt[] prodChan = {Channel.one2oneInt(), Channel.one2oneInt()};
        final One2OneChannelInt[] consReq = {Channel.one2oneInt(), Channel.one2oneInt()};
        final One2OneChannelInt[] consChan = {Channel.one2oneInt(), Channel.one2oneInt()};
        CSProcess[] procList = {new Producer2(prodChan[0], 0), new Producer2(prodChan[1], 100), new Buffer(prodChan,
            consReq,
            consChan
        ), new Consumer2(consReq[0], consChan[0]), new Consumer2(consReq[1], consChan[1])};
        Parallel par = new Parallel(procList);
        par.run();
    }
}