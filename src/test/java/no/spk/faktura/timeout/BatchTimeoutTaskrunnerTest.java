package no.spk.faktura.timeout;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofNanos;
import static java.time.LocalTime.now;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ScheduledFuture;

import org.junit.jupiter.api.Test;

public class BatchTimeoutTaskrunnerTest {
    /*
     * Verifiserer at callback-metode blir brukt
     */
    @Test
    public void testTerminationTimeout() throws Exception {
        final Runnable callback = mock(Runnable.class);
        final BatchTimeout batchTimeout = new BatchTimeout(ofNanos(0), now());
        batchTimeout.start();
        final BatchTimeoutTaskrunner termination = new BatchTimeoutTaskrunner(batchTimeout);
        final ScheduledFuture<?> scheduledFuture = termination.startTerminationTimeout(ofMinutes(0), callback);
        scheduledFuture.get();
        verify(callback).run();
    }
}
