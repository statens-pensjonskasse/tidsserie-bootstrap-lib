package no.spk.tidsserie.input;

import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class ReadableFileValidator {

    public void validate(final String name, final Path value, final CommandSpec spec) throws ParameterException {
        new ReadablePathValidator().validate(name, value, spec);

        if (!Files.isRegularFile(value)) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Stien "
                            + value
                            + " peker ikke til en fil, verifiser at du har angitt rett filsti."
            );
        }
    }
}
