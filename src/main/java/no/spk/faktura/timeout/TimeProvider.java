package no.spk.faktura.timeout;

import java.time.LocalTime;

/**
 * TimeProvider gir fra seg n�v�rende LocalTime og n�v�rende millisekunder, og brukes av BatchTimeout.
 * Kan brukes til � kontrollere om tid er l�pende eller statisk avhenig av implementasjon.
 * @see BatchTimeout
 * @author Snorre E. Brekke - Computas
 */
public interface TimeProvider {
    /**
     * @return LocalTime som representerer 'n�' tidspunkt. M� ikke v�re virkelig tid.
     * Det er rimelig � anta at delta mellom to p�f�lgende kall til currentTime() &gt;= 0;
     */
    LocalTime currentTime();

    /**
     * @return antall millisekunder som representerer 'n�'.
     * Det er rimelig � anta at delta mellom to p�f�lgende kall til currentMillies() &gt;= 0;
     */
    long currentMillies();
}
