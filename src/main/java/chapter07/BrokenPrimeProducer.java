package chapter07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BrokenPrimeProducer
 * <p/>
 * Unreliable cancellation that can leave producers stuck in a blocking operation
 *
 * @author Brian Goetz and Tim Peierls
 */
class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled)
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException consumed) {
        }
    }

    public void cancel() {
        cancelled = true;
    }

    void consumePrimes() throws InterruptedException {
        BlockingQueue<BigInteger> primes = new LinkedBlockingQueue<>();
        BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);
        producer.start();
        try {
            while (needMorePrimes()) {
                consume(primes.take());
            }
        } finally {
            producer.cancel();
        }
    }

    private void consume(BigInteger prime) {
    }

    private boolean needMorePrimes() {
        return false;
    }
}
