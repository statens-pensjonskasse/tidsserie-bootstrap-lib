package no.spk.faktura.input;

import java.time.format.DateTimeParseException;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

/**
 * Validerer at angitt verdi lar seg konvertere med {@link LocalTimeConverter#convert(String)}
 */
public class LocalTimeValidator {
    public void validate(final String name, final String value) throws ParameterException {
        try {
            new LocalTimeConverter().convert(value);
        } catch (DateTimeParseException e) {
            throw new ParameterException(
                    new CommandLine(new DummyCommand()),
                    "'" + name + "': er ikke en gyldig tidsverdi (fant " + value + ")."
            );
        }
    }
}
