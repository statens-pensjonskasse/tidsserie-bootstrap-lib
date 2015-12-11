package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class ReadablePathValidator implements IValueValidator<Path> {
    @Override
    public void validate(final String name, final Path value) throws ParameterException {
        //Files.exists(value) viser seg å returnerer false i noen tilfeller  der file.exists returnerer true.
        if (!value.toFile().exists()) {
            throw new ParameterException(
                    "Filen "
                            + value
                            + " eksisterer ikke, verifiser at du har angitt rett filsti."
            );
        }


        if (!Files.isReadable(value)) {
            throw new ParameterException(
                    "Filen "
                            + value
                            + " er ikke lesbar for batchen, verifiser at batchbrukeren har lesetilgang til filen."
            );
        }
    }
}
