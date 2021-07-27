package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class PathValidator {
    public void validate(final String name, final Optional<Path> value, final CommandSpec spec) throws ParameterException {
        if (!value.isPresent()) {
            return;
        }

        if (!Files.exists(value.get())) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Filen "
                            + value.get()
                            + " eksisterer ikke, verifiser at du har angitt rett filnavn og -sti."
            );
        }

        if (!Files.isReadable(value.get())) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Filen "
                            + value.get()
                            + " er ikke lesbar for batchen, verifiser at batchbrukeren har lesetilgang til filen."
            );
        }
    }
}
