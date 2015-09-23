package no.spk.faktura.timeout;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * BatchTimeout brukes for å styre når batchen skal stoppes, dersom den tar for lang tid.
 *
 * <p>
 * Det brukes to tidsaspekter for å bestemme timeout: <br>
 * Timeout oppgitt i antall sekunder,
 * og timeout gitt som et absolutt tidspunkt på dagen når batchen skal stoppe.
 * </p>
 * <p>
 * BatchTimeout beregner minste timeout for batchen basert på disse to tids-verdiene, beregnet
 * fra tidspunktet BatchTimeout blir konstruert.
 * Dvs. at klokka for timeout kan begynne å løpe før batchen starter, dersom BatchTimeout
 * blir initsialisert før batchen er startet. Dette bør ikke være noe stort problem i praksis,
 * da presisjonen på timeout er "best-effort".
 * </p>
 * @author Snorre E. Brekke - Computas
 */
public class BatchTimeout {
    private final long timeout;
    private final long timeStarted;
    private final TimeProvider timeProvider;

    /**
     * Lager og starter klokka for batch-timeout for batch. Faktisk timeout som brukes ved kjøring
     * beregnes fra tidspunktet BatchTimeout-instansen blir initisialisert.
     * @param timeout maks tid batchen kan kjøre
     * @param latestEndtime siste tidspunkt batchen kan kjøre til. Dersom dette i praksis resulterer
     * i et kortere tidsintervall enn {@code timeout} så blir timeout for batcehn kalkulert utifra LocalTime.now().
     */
    public BatchTimeout(Duration timeout, LocalTime latestEndtime) {
        this(timeout, latestEndtime, new DefaultTimeProvider());
    }

    /**
     * Lager og starter klokka for batch-timeout for batch. Faktisk timeout som brukes ved kjøring
     * beregnes fra tidspunktet BatchTimeout-instansen blir initisialisert.
     * @param timeout maks tid batchen kan kjøre
     * @param latestEndtime siste tidspunkt batchen kan kjøre til. Dersom dette i praksis resulterer
     * i et kortere tidsintervall enn {@code timeout} så blir timeout for batcehn kalkulert utifra timeProvider.currentTime().
     * @param timeProvider gir mulighet til å overstyre hvordan BatchTimeout finner nårværende tid og systemklokke.
     */
    public BatchTimeout(Duration timeout, LocalTime latestEndtime, TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        long timeToEnd = ChronoUnit.MILLIS.between(timeProvider.currentTime(), latestEndtime);
        this.timeout = Math.min(timeout.toMillis(), timeToEnd);
        timeStarted = timeProvider.currentMillies();
    }

    /**
     * @return Returnerer gjenværende tidsintervall for batchen.
     */
    public Duration timeRemaining() {
        return Duration.ofMillis(timeStarted + timeout - timeProvider.currentMillies());
    }

    /**
     * @return true dersom kjøretid for batchen har oversteget timeoutverdien til BatchTimeout
     */
    public boolean isComplete() {
        return (timeProvider.currentMillies() - timeStarted) >= timeout;
    }

    TimeProvider getTimeProvider() {
        return timeProvider;
    }
}
