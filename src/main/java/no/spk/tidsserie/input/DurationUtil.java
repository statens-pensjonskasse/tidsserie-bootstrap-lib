package no.spk.tidsserie.input;

import static java.lang.Integer.parseInt;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.time.Duration;
import java.util.Optional;

/**
 * Util-klasse for å jobbe med {@link Duration}.
 */
public final class DurationUtil {
    private DurationUtil() {
    }

    /**
     * Konverterer en streng på formatet HHmm til en Optional {@link Duration}.
     *
     * @param duration streng på formated HHmm
     * @return {@code duration} som en {@link Duration}. {@link Optional#empty()} dersom {@code duration} paramteren ikke kan konverteres til
     * en {@link Duration}, eller angitt verdi er negativ.
     */
    public static Optional<Duration> convert(final String duration) {
        if (duration.length() != 4) {
            return empty();
        }
        try {
            final int hours = parseInt(duration.substring(0, 2));
            final int minutes = parseInt(duration.substring(2, 4));
            final Duration durationValue = Duration.of(hours, HOURS).plus(Duration.of(minutes, MINUTES));
            if (durationValue.isNegative()) {
                return empty();
            }
            return of(durationValue);
        } catch (NumberFormatException e) {
            return empty();
        }
    }
}
