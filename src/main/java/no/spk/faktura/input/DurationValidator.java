package no.spk.faktura.input;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class DurationValidator {
    public void validate(final String name, final String value) throws ParameterException {
        DurationUtil.convert(value)
                .orElseThrow(() -> new ParameterException(
                        new CommandLine(new DummyCommand()),
                        "'" + name + "': må bestå av 4 siffer på formatet HHmm (fant " + value + ").")
                );
    }
}
