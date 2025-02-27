package no.spk.tidsserie.timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Denne klassen er siste utvei for å avslutt batchen dersom den bruker for lang tid.
 */
public class BatchTimeoutTaskrunner {
    private BatchTimeout batchTimeout;

    public BatchTimeoutTaskrunner(final BatchTimeout batchTimeout) {
        this.batchTimeout = batchTimeout;
    }

    /**
     * Planlegger en oppgave som vil kjøre {@code terminationCallback} etter BatchTimeout pluss {@code timeoutDelay}.
     *
     * @param timeoutDelay verdi som skal legges til BatchTimeout før terminationCallback bli kjørt. Nyttig for å la andre timeout-mekanismer
     * få tid til å agere først.
     * @param terminationCallback callback som skal kalles ved timeout
     * @return {@link ScheduledFuture} som representerer den planlagte oppgaven.
     * @see BatchTimeout
     * @see ScheduledFuture
     */
    public ScheduledFuture<?> startTerminationTimeout(final Duration timeoutDelay, final Runnable terminationCallback) {
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
