package no.spk.faktura.input;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class IntegerValidator {
    public void validate(final String name, final String value, final CommandLine cmd) throws ParameterException {
        try {
            Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new ParameterException(cmd, "'" + name + "': er ikke et gyldig tall (fant " + value + ").");
        }
    }
}
