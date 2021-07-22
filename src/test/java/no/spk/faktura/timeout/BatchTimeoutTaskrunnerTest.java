package no.spk.faktura.timeout;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofNanos;
import static java.time.LocalTime.now;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ScheduledFuture;

import org.junit.Test;

public class BatchTimeoutTaskrunnerTest {
    /*
     * Verifiserer at callback-metode blir brukt
     */
    @Test
    public void testTerminationTimeout() throws Exception {
        Runnable callback = mock(Runnable.class);
        BatchTimeout batchTimeout = new BatchTimeout(ofNanos(0), now());
        batchTimeout.start();
        BatchTimeoutTaskrunner termination = new BatchTimeoutTaskrunner(batchTimeout);
        ScheduledFuture<?> scheduledFuture = termination.startTerminationTimeout(ofMinutes(0), callback);
        scheduledFuture.get();
        verify(callback).run();
    }
}
