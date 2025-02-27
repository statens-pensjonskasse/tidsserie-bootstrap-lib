package no.spk.tidsserie.input;

import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class WritableFileValidator {

    public void validate(final String name, final Path value, final CommandSpec spec) throws ParameterException {
        new ReadableFileValidator().validate(name, value, spec);

        if (!Files.isWritable(value)) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Filen "
                            + value
                            + " er ikke skrivbar for batchen, verifiser at batchbrukeren har skrivetilgang til filen."
            );
        }
    }
}
