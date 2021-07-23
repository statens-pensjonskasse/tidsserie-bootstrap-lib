package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class ReadableFileValidator {
    public void validate(final String name, final Path value) throws ParameterException {
        new ReadablePathValidator().validate(name, value);

        if (!Files.isRegularFile(value)) {
            throw new ParameterException(
                    new CommandLine(new DummyCommand()),
                    "Stien "
                            + value
                            + " peker ikke til en fil, verifiser at du har angitt rett filsti."
            );
        }
    }
}
