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

/**
 * @author Snorre E. Brekke - Computas
 */
public class BatchTimeoutTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testIsCompleteCalledOnUnstartedTimeoutThrowsException() throws Exception {
        exception.expectMessage("BatchTimeout er ikke startet.");
        BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        batchTimeout.isComplete();
    }

    @Test
    public void testTimeRemainingCalledOnUnstartedTimeoutThrowsException() throws Exception {
        exception.expectMessage("BatchTimeout er ikke startet.");
        BatchTimeout batchTimeout = createBatchTimeout("02:00", "02:00", ofMinutes(0));
        batchTimeout.timeRemaining();
    }

    @Test
    public void testNoTimeoutCompletes() throws Exception {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:00", ofMinutes(0));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    public void testSomeTimeoutSameEndtimeAsStarttimeCompletes() throws Exception {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:00", ofMinutes(1));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    public void testZeroTimeoutSomeEndtimeCompletes() throws Exception {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("02:00", "02:01", ofMinutes(0));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    @Test
    public void testEqualTimeoutAndEndtimeWillEventuallyComplete() throws Exception {
        BatchTimeout batchTimeout = createBatchTimeoutAndStart("03:00", "03:01", ofMinutes(1));
        ConstantTimeProvider timeProvider = (ConstantTimeProvider) batchTimeout.getTimeProvider();

        assertThat(batchTimeout.isComplete()).isFalse();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(1, MINUTES));

        timeProvider.setMillies(timeProvider.currentMillies() + ofMinutes(1).toMillis());
        timeProvider.setTime(timeProvider.currentTime().plus(1, MINUTES));
        assertThat(batchTimeout.isComplete()).isTrue();
        assertThat(batchTimeout.timeRemaining()).isEqualTo(of(0, MILLIS));
    }

    private BatchTimeout createBatchTimeoutAndStart(String startTime, String latestEndTime, Duration timeout) {
        return createBatchTimeout(startTime, latestEndTime, timeout).start();
    }

    private BatchTimeout createBatchTimeout(String startTime, String latestEndTime, Duration timeout) {
        LocalTime localStartTime = LocalTime.parse(startTime);
        LocalTime localEndTime = LocalTime.parse(latestEndTime);
        TimeProvider timeProvider = getNowTimeProvider(localStartTime);
        return new BatchTimeout(timeout, localEndTime, timeProvider);
    }

    private ConstantTimeProvider getNowTimeProvider(LocalTime startTime) {
        return new ConstantTimeProvider(startTime, 0);
    }

}