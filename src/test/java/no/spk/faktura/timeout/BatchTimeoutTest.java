package no.spk.faktura.timeout;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;

import static java.time.Duration.of;
import static java.time.Duration.ofMinutes;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BatchTimeoutTest {

    @Test
    public void testNewBatchTimeoutIsNotStarted() {
        BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        assertThat(batchTimeout.isStarted()).isFalse();
    }

    @Test
    public void testStartedBatchTimeoutIsStarted() {
        BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        batchTimeout.start();
        assertThat(batchTimeout.isStarted()).isTrue();
    }

    @Test
    public void testBatchTimeoutStartedTwiceThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
            batchTimeout.start();
            batchTimeout.start();
        });
        assertTrue(exception.getMessage().contains( "BatchTimeout er er allerede startet."));
    }

    @Test
    public void testIsCompleteCalledOnUnstartedTimeoutThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
            batchTimeout.isComplete();
        });
        assertTrue(exception.getMessage().contains( "BatchTimeout er ikke startet."));
    }

    @Test
    public void testTimeRemainingCalledOnUnstartedTimeoutThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
            batchTimeout.timeRemaining();
        });
        assertTrue(exception.getMessage().contains( "BatchTimeout er ikke startet."));
    }

    @Test
    public void testNoTimeoutCompletes() {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:00", ofMinutes(0));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    public void testSomeTimeoutSameEndtimeAsStarttimeCompletes() {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:00", ofMinutes(1));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    public void testZeroTimeoutSomeEndtimeCompletes() {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:01", ofMinutes(0));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    public void testEqualTimeoutAndEndtimeWillEventuallyComplete() {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("03:00", "03:01", ofMinutes(1));
        ConstantTimeProvider timeProvider = (ConstantTimeProvider) batchTimeout.getTimeProvider();

        assertThat(batchTimeout.isComplete()).isFalse();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(1, MINUTES));

        timeProvider.setMillies(timeProvider.currentMillies() + ofMinutes(1).toMillis());
        timeProvider.setTime(timeProvider.currentTime().plus(1, MINUTES));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    private BatchTimeout createBatchTimeoutAndStart(final String startTime, final String latestEndTime, final Duration timeout) {
        return createBatchTimeout(startTime, latestEndTime, timeout).start();
    }

    private BatchTimeout createBatchTimeout(final String startTime, final String latestEndTime, final Duration timeout) {
        LocalTime localStartTime = LocalTime.parse(startTime);
        LocalTime localEndTime = LocalTime.parse(latestEndTime);
        TimeProvider timeProvider = getNowTimeProvider(localStartTime);
        return new BatchTimeout(timeout, localEndTime, timeProvider);
    }

    private ConstantTimeProvider getNowTimeProvider(final LocalTime startTime) {
        return new ConstantTimeProvider(startTime, 0);
    }
}
