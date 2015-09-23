package no.spk.faktura.timeout;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * BatchTimeout brukes for � styre n�r batchen skal stoppes, dersom den tar for lang tid.
 *
 * <p>
 * Det brukes to tidsaspekter for � bestemme timeout: <br>
 * Timeout oppgitt i antall sekunder,
 * og timeout gitt som et absolutt tidspunkt p� dagen n�r batchen skal stoppe.
 * </p>
 * <p>
 * BatchTimeout beregner minste timeout for batchen basert p� disse to tids-verdiene, beregnet
 * fra tidspunktet BatchTimeout blir konstruert.
 * Dvs. at klokka for timeout kan begynne � l�pe f�r batchen starter, dersom BatchTimeout
 * blir initsialisert f�r batchen er startet. Dette b�r ikke v�re noe stort problem i praksis,
 * da presisjonen p� timeout er "best-effort".
 * </p>
 * @author Snorre E. Brekke - Computas
 */
public class BatchTimeout {
    private final long timeout;
    private final long timeStarted;
    private final TimeProvider timeProvider;

    /**
     * Lager og starter klokka for batch-timeout for batch. Faktisk timeout som brukes ved kj�ring
     * beregnes fra tidspunktet BatchTimeout-instansen blir initisialisert.
     * @param timeout maks tid batchen kan kj�re
     * @param latestEndtime siste tidspunkt batchen kan kj�re til. Dersom dette i praksis resulterer
     * i et kortere tidsintervall enn {@code timeout} s� blir timeout for batcehn kalkulert utifra LocalTime.now().
     */
    public BatchTimeout(Duration timeout, LocalTime latestEndtime) {
        this(timeout, latestEndtime, new DefaultTimeProvider());
    }

    /**
     * Lager og starter klokka for batch-timeout for batch. Faktisk timeout som brukes ved kj�ring
     * beregnes fra tidspunktet BatchTimeout-instansen blir initisialisert.
     * @param timeout maks tid batchen kan kj�re
     * @param latestEndtime siste tidspunkt batchen kan kj�re til. Dersom dette i praksis resulterer
     * i et kortere tidsintervall enn {@code timeout} s� blir timeout for batcehn kalkulert utifra timeProvider.currentTime().
     * @param timeProvider gir mulighet til � overstyre hvordan BatchTimeout finner n�rv�rende tid og systemklokke.
     */
    public BatchTimeout(Duration timeout, LocalTime latestEndtime, TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        long timeToEnd = ChronoUnit.MILLIS.between(timeProvider.currentTime(), latestEndtime);
        this.timeout = Math.min(timeout.toMillis(), timeToEnd);
        timeStarted = timeProvider.currentMillies();
    }

    /**
     * @return Returnerer gjenv�rende tidsintervall for batchen.
     */
    public Duration timeRemaining() {
        return Duration.ofMillis(timeStarted + timeout - timeProvider.currentMillies());
    }

    /**
     * @return true dersom kj�retid for batchen har oversteget timeoutverdien til BatchTimeout
     */
    public boolean isComplete() {
        return (timeProvider.currentMillies() - timeStarted) >= timeout;
    }

    TimeProvider getTimeProvider() {
        return timeProvider;
    }
}
