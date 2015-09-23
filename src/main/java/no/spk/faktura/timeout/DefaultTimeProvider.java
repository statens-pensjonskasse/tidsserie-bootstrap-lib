package no.spk.faktura.timeout;

import java.time.LocalTime;

/**
 * Default implementasjon av TimeProvider. Bruker LocalTime.now() og System.currentMillies() til � bestemme tid.
 * Dette betyr at klasser som baserer seg p� bruk av denne implementasjonen vil f� forskjellig resultat for hvert kall til metodene.,
 * @author Snorre E. Brekke - Computas
 */
public class DefaultTimeProvider implements TimeProvider{

    @Override
    public LocalTime currentTime() {
        return LocalTime.now();
    }

    @Override
    public long currentMillies() {
        return System.currentTimeMillis();
    }
}
