package no.spk.tidsserie.input;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class DurationValidator {
    public void validate(final String name, final String value, final CommandSpec spec) throws ParameterException {
        DurationUtil.convert(value)
                .orElseThrow(() -> new ParameterException(
                        new CommandLine(spec),
                        "'" + name + "': må bestå av 4 siffer på formatet HHmm (fant " + value + ").")
                );
    }
}
