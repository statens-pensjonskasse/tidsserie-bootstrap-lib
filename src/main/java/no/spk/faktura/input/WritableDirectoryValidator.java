package no.spk.faktura.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class WritableDirectoryValidator {

    public void validate(final String name, final Path value, final CommandSpec spec) throws ParameterException {
        // Files.exists(value) viser seg å returnerer false i noen tilfeller  der file.exists returnerer true.
        if (!value.toFile().exists()) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Katalogen "
                            + value
                            + " eksisterer ikke, verifiser at du har angitt rett katalogsti."
            );
        }

        if (!Files.isDirectory(value)) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Stien "
                            + value
                            + " peker ikke til en katalog, verifiser at du har angitt rett katalogsti."
            );
        }

        if (!Files.isReadable(value)) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Innholdet i katalogen "
                            + value
                            + " er ikke lesbar for batchen, verifiser at batchbrukeren har lesetilgang til katalogen."
            );
        }

        try {
            // Vi kan ikke stole på at Files.isWriteable(path) rapporterer riktig, og skriver derfor heller en testfil for å sjekke skrivetilgang
            final Path testWriteFile = value.resolve("testwrite.txt");
            Files.createFile(testWriteFile);
            Files.delete(testWriteFile);
        } catch (final IOException e) {
            throw new ParameterException(
                    new CommandLine(spec),
                    "Katalogen "
                            + value
                            + " er ikke skrivbar for batchen, verifiser at batchbrukeren har skrivetilgang til katalogen."
            );
        }
    }
}
