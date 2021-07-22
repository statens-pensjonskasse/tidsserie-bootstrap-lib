package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class ReadableFileValidator {
    public void validate(final String name, final Path value, final CommandLine cmd) throws ParameterException {
        new ReadablePathValidator().validate(name, value, cmd);

        if (!Files.isRegularFile(value)) {
            throw new ParameterException(
                    cmd,
                    "Stien "
                            + value
                            + " peker ikke til en fil, verifiser at du har angitt rett filsti."
            );
        }
    }
}
