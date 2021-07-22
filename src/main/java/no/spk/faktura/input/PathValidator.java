package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class PathValidator {
    public void validate(final String name, final Optional<Path> value, final CommandLine cmd) throws ParameterException {
        if (!value.isPresent()) {
            return;
        }

        if (!Files.exists(value.get())) {
            throw new ParameterException(
                    cmd,
                    "Filen "
                            + value.get()
                            + " eksisterer ikke, verifiser at du har angitt rett filnavn og -sti."
            );
        }

        if (!Files.isReadable(value.get())) {
            throw new ParameterException(
                    cmd,
                    "Filen "
                            + value.get()
                            + " er ikke lesbar for batchen, verifiser at batchbrukeren har lesetilgang til filen."
            );
        }
    }
}
