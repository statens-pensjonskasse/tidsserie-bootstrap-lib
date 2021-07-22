package no.spk.faktura.timeout;

import java.time.LocalTime;

/**
 * Default implementasjon av TimeProvider. Bruker LocalTime.now() og System.currentMillies() til å bestemme tid.
 * Dette betyr at klasser som baserer seg på bruk av denne implementasjonen vil få forskjellig resultat for hvert kall til metodene.,
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
