package no.spk.faktura.input;

import java.time.LocalTime;

import com.beust.jcommander.IStringConverter;

/**
 * Konverterer et kommandolinjeargument til {@link LocalTime}
 * @author Snorre E. Brekke - Computas
 * @see com.beust.jcommander.JCommander
 */
public class LocalTimeConverter implements IStringConverter<LocalTime> {
    @Override
    public LocalTime convert(String value) {
        return LocalTime.parse(value);
    }
}
