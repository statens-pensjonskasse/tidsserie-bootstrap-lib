package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class WritableFileValidator implements IValueValidator<Path> {
    @Override
    public void validate(final String name, final Path value) throws ParameterException {
        new ReadableFileValidator().validate(name, value);

        if (!Files.isWritable(value)) {
            throw new ParameterException(
                    "Filen "
                            + value
                            + " er ikke skrivbar for batchen, verifiser at batchbrukeren har skrivetilgang til filen."
            );
        }
    }
}
