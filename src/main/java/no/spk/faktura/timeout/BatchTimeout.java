package no.spk.faktura.timeout;

import static java.util.Optional.empty;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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

    private final Duration maxRuntime;
    private final LocalTime latestEndtime;
    private final TimeProvider timeProvider;
    private long timeout;
    private Optional<Long> timeStarted = empty();

    /**
     * Opppretter batch-timeout for en batch. Timeout som brukes ved kjøring
     * beregnes fra maxRuntime og latestEndtime. Timeout-klokka startes ved å kalle {@link #start()}.
     * @param maxRuntime maks tid batchen kan kjøre
     * @param latestEndtime siste tidspunkt batchen kan kjøre til. Dersom dette i praksis resulterer
     * i et kortere tidsintervall enn {@code timeout} så blir timeout for batcehn kalkulert utifra LocalTime.now().
     */
    public BatchTimeout(Duration maxRuntime, LocalTime latestEndtime) {
        this(maxRuntime, latestEndtime, new DefaultTimeProvider());
    }

    /**
     * Opppretter batch-timeout for en batch. Timeout som brukes ved kjøring
     * beregnes fra maxRuntime og latestEndtime. Timeout-klokka startes ved å kalle {@link #start()}.
     * @param maxRuntime maks tid batchen kan kjøre
     * @param latestEndtime siste tidspunkt batchen kan kjøre til. Dersom dette i praksis resulterer
     * i et kortere tidsintervall enn {@code timeout} så blir timeout for batcehn kalkulert utifra LocalTime.now().
     * @param timeProvider gir mulighet til å overstyre hvordan BatchTimeout finner nårværende tid og systemklokke.
     */
    public BatchTimeout(Duration maxRuntime, LocalTime latestEndtime, TimeProvider timeProvider) {
        this.maxRuntime = maxRuntime;
        this.latestEndtime = latestEndtime;
        this.timeProvider = timeProvider;
    }

    /**
     * Starter klokka for timeout. Denne metoden må kalles i forkant av {@link #timeRemaining()} og {@link #isComplete()}.
     * @return this for chaining
     * @throws IllegalStateException dersom denne metoden kalles før mer enn én gang.
     * @see #isStarted()
     */
    public BatchTimeout start() {
        if (isStarted()) {
            throw new IllegalStateException("BatchTimeout er er allerede startet.");
        }
        long timeToEnd = ChronoUnit.MILLIS.between(timeProvider.currentTime(), latestEndtime);
        this.timeout = Math.min(maxRuntime.toMillis(), timeToEnd);
        timeStarted = Optional.of(timeProvider.currentMillies());
        return this;
    }

    /**
     * Angir om {@link #start()} er blitt kalt.
     * @return true dersom {@link #start()} er blitt kalt.
     */
    public boolean isStarted() {
        return timeStarted.isPresent();
    }

    /**
     * @return Returnerer gjenværende tidsintervall for batchen.
     * @throws IllegalStateException dersom denne metoden kalles før {@link #start} er blitt kalt
     */
    public Duration timeRemaining() {
        return Duration.ofMillis(timeStarted() + timeout - timeProvider.currentMillies());
    }

    private Long timeStarted() {
        return timeStarted.orElseThrow(() -> new IllegalStateException("BatchTimeout er ikke startet. Vennligst kall metoden #start() først."));
    }

    /**
     * @return true dersom kjøretid for batchen har oversteget timeoutverdien til BatchTimeout
     * @throws IllegalStateException dersom denne metoden kalles før {@link #start} er blitt kalt
     */
    public boolean isComplete() {
        return (timeProvider.currentMillies() - timeStarted()) >= timeout;
    }

    TimeProvider getTimeProvider() {
        return timeProvider;
    }
}
