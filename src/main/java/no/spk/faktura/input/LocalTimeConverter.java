package no.spk.faktura.input;

import static java.time.temporal.TemporalQueries.localTime;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;
import java.util.regex.Pattern;

import com.beust.jcommander.IStringConverter;

/**
 * Konverterer et kommandolinjeargument til {@link LocalTime}.
 * Støtter formatene HHmm, HHmmss, HH:mm HH:mm:ss. Typisk ønsker vi å eksponere kun de to første i brukerveiledningen.
 * @author Snorre E. Brekke - Computas
 * @see com.beust.jcommander.JCommander
 */
public class LocalTimeConverter implements IStringConverter<LocalTime> {
    private static final DateTimeFormatter COMPACT_MINUTES_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
    private static final DateTimeFormatter COMPACT_SECONDS_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    /**
     * Støtter formatene HHm, HHmmss HH:mm HH:mm:ss. Typisk ønsker vi å eksponere kun de to første i brukerveiledningen.
     * @param value Verdien som skal konverteres til {@link LocalTime}
     * @return {@code value} konvertert til {@link LocalTime}
     */
    @Override
    public LocalTime convert(String value) {
        if (!value.contains(":")) {
            if (value.length() == 4) {
                return COMPACT_MINUTES_FORMATTER.parse(value, localTime());
            } else if(value.length() == 6) {
                return COMPACT_SECONDS_FORMATTER.parse(value, localTime());
            }
        }
        return LocalTime.parse(value);
    }
}
