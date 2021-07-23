package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class ReadablePathValidator {
    public void validate(final String name, final Path value) throws ParameterException {
        //Files.exists(value) viser seg å returnerer false i noen tilfeller  der file.exists returnerer true.
        if (!value.toFile().exists()) {
            throw new ParameterException(
                    new CommandLine(new DummyCommand()),
                    "Filen "
                            + value
                            + " eksisterer ikke, verifiser at du har angitt rett filsti."
            );
        }


        if (!Files.isReadable(value)) {
            throw new ParameterException(
                    new CommandLine(new DummyCommand()),
                    "Filen "
                            + value
                            + " er ikke lesbar for batchen, verifiser at batchbrukeren har lesetilgang til filen."
            );
        }
    }
}
