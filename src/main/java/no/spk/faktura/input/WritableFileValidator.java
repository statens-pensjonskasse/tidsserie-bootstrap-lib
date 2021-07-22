package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class WritableFileValidator {
    public void validate(final String name, final Path value, final CommandLine cmd) throws ParameterException {
        new ReadableFileValidator().validate(name, value, cmd);

        if (!Files.isWritable(value)) {
            throw new ParameterException(
                    cmd,
                    "Filen "
                            + value
                            + " er ikke skrivbar for batchen, verifiser at batchbrukeren har skrivetilgang til filen."
            );
        }
    }
}
