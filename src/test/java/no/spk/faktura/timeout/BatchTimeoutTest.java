package no.spk.faktura.timeout;

import static java.time.Duration.of;
import static java.time.Duration.ofMinutes;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BatchTimeoutTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

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
        exception.expectMessage("BatchTimeout er er allerede startet.");
        BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        batchTimeout.start();
        batchTimeout.start();
    }

    @Test
    public void testIsCompleteCalledOnUnstartedTimeoutThrowsException() {
        exception.expectMessage("BatchTimeout er ikke startet.");
        BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        batchTimeout.isComplete();
    }

    @Test
    public void testTimeRemainingCalledOnUnstartedTimeoutThrowsException() {
        exception.expectMessage("BatchTimeout er ikke startet.");
        BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        batchTimeout.timeRemaining();
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
