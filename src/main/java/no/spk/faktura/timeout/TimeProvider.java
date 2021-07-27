package no.spk.faktura.timeout;

import java.time.LocalTime;

/**
 * TimeProvider gir fra seg nåværende LocalTime og nåværende millisekunder, og brukes av BatchTimeout.
 * Kan brukes til å kontrollere om tid er løpende eller statisk avhenig av implementasjon.
 *
 * @see BatchTimeout
 */
public interface TimeProvider {
    /**
     * @return LocalTime som representerer 'nå' tidspunkt. Må ikke være virkelig tid.
     * Det er rimelig å anta at delta mellom to påfølgende kall til currentTime() &gt;= 0;
     */
    LocalTime currentTime();

    /**
     * @return antall millisekunder som representerer 'nå'.
     * Det er rimelig å anta at delta mellom to påfølgende kall til currentMillies() &gt;= 0;
     */
    long currentMillies();
}
