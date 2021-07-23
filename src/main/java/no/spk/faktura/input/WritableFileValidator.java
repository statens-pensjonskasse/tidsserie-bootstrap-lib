package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class WritableFileValidator {
    public void validate(final String name, final Path value) throws ParameterException {
        new ReadableFileValidator().validate(name, value);

        if (!Files.isWritable(value)) {
            throw new ParameterException(
                    new CommandLine(new DummyCommand()),
                    "Filen "
                            + value
                            + " er ikke skrivbar for batchen, verifiser at batchbrukeren har skrivetilgang til filen."
            );
        }
    }
}
