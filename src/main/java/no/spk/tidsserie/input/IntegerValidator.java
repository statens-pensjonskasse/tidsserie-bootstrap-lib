package no.spk.tidsserie.input;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class IntegerValidator {

    public void validate(final String name, final String value, final CommandSpec spec) throws ParameterException {
        try {
            Integer.parseInt(value);
        } catch (final NumberFormatException e) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "'" + name + "': er ikke et gyldig tall (fant " + value + ")."
            );
        }
    }
}
