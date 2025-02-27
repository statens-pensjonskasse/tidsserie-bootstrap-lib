package no.spk.tidsserie.timeout;

import static java.time.Duration.of;
import static java.time.Duration.ofMinutes;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class BatchTimeoutTest {

    @Test
    void testNewBatchTimeoutIsNotStarted() {
        final BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        assertThat(batchTimeout.isStarted()).isFalse();
    }

    @Test
    void testStartedBatchTimeoutIsStarted() {
        final BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        batchTimeout.start();
        assertThat(batchTimeout.isStarted()).isTrue();
    }

    @Test
    void testBatchTimeoutStartedTwiceThrowsException() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            final BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
            batchTimeout.start();
            batchTimeout.start();
        });
        assertTrue(exception.getMessage().contains("BatchTimeout er er allerede startet."));
    }

    @Test
    void testIsCompleteCalledOnUnstartedTimeoutThrowsException() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            final BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
            batchTimeout.isComplete();
        });
        assertTrue(exception.getMessage().contains("BatchTimeout er ikke startet."));
    }

    @Test
    void testTimeRemainingCalledOnUnstartedTimeoutThrowsException() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            final BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
            batchTimeout.timeRemaining();
        });
        assertTrue(exception.getMessage().contains("BatchTimeout er ikke startet."));
    }

    @Test
    void testNoTimeoutCompletes() {
        final BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:00", ofMinutes(0));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    void testSomeTimeoutSameEndtimeAsStarttimeCompletes() {
        final BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:00", ofMinutes(1));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    void testZeroTimeoutSomeEndtimeCompletes() {
        final BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:01", ofMinutes(0));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    void testEqualTimeoutAndEndtimeWillEventuallyComplete() {
        final BatchTimeout batchTimeout = createBatchTimeoutAndStart("03:00", "03:01", ofMinutes(1));
        final ConstantTimeProvider timeProvider = (ConstantTimeProvider) batchTimeout.getTimeProvider();

        assertThat(batchTimeout.isComplete()).isFalse();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(1, MINUTES));

        timeProvider.setMillies(timeProvider.currentMillies() + ofMinutes(1).toMillis());
        timeProvider.setTime(timeProvider.currentTime().plusMinutes(1));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    private BatchTimeout createBatchTimeoutAndStart(final String startTime, final String latestEndTime, final Duration timeout) {
        return createBatchTimeout(startTime, latestEndTime, timeout).start();
    }

    private BatchTimeout createBatchTimeout(final String startTime, final String latestEndTime, final Duration timeout) {
        final LocalTime localStartTime = LocalTime.parse(startTime);
        final LocalTime localEndTime = LocalTime.parse(latestEndTime);
        final TimeProvider timeProvider = getNowTimeProvider(localStartTime);
        return new BatchTimeout(timeout, localEndTime, timeProvider);
    }

    private ConstantTimeProvider getNowTimeProvider(final LocalTime startTime) {
        return new ConstantTimeProvider(startTime, 0);
    }
}
