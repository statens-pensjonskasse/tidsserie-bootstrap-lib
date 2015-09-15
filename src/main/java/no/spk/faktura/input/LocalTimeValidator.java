package no.spk.faktura.input;

import java.time.format.DateTimeParseException;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Validerer at angitt verdi lar seg konvertere med {@link LocalTimeConverter#convert(String)}
 * @author Snorre E. Brekke - Computas
 */
public class LocalTimeValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            new LocalTimeConverter().convert(value);
        } catch (DateTimeParseException e) {
            throw new ParameterException("'" + name + "': er ikke en gyldig tidsverdi (fant " + value + ").");
        }
    }
}
