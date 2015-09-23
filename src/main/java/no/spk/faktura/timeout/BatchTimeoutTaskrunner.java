package no.spk.faktura.timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Denne klassen er siste utvei for � avslutt batchen dersom den bruker for lang tid.
 *
 * @author Snorre E. Brekke - Computas
 */
public class BatchTimeoutTaskrunner {
    private BatchTimeout batchTimeout;
    public BatchTimeoutTaskrunner(BatchTimeout batchTimeout) {
        this.batchTimeout = batchTimeout;
    }

    /**
     * Planlegger en oppgave som vil kj�re {@code terminationCallback} etter BatchTimeout pluss {@code timeoutDelay}.
     *
     * @param timeoutDelay verdi som skal legges til BatchTimeout f�r terminationCallback bli kj�rt. Nyttig for � la andre timeout-mekanismer
     * f� tid til � agere f�rst.
     * @param terminationCallback callback som skal kalles ved timeout
     * @return {@link ScheduledFuture} som representerer den planlagte oppgaven.
     * @see BatchTimeout
     * @see ScheduledFuture
     */
    public ScheduledFuture<?> startTerminationTimeout(Duration timeoutDelay, Runnable terminationCallback) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread timeoutTaskrunner = new Thread(r, "BatchTimeoutTaskrunner");
            timeoutTaskrunner.setDaemon(true);
            return timeoutTaskrunner;
        });
        return executor.schedule(terminationCallback::run, getTimeoutInMilliseconds(timeoutDelay), MILLISECONDS);

    }

    private long getTimeoutInMilliseconds(Duration timeoutDelay) {
        return batchTimeout.timeRemaining().plus(timeoutDelay).toMillis();
    }
}
