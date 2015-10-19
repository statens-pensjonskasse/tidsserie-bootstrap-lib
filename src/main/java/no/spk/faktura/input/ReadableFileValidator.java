package no.spk.faktura.input;

import java.nio.file.Files;
import java.nio.file.Path;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class ReadableFileValidator implements IValueValidator<Path> {
    @Override
    public void validate(final String name, final Path value) throws ParameterException {
        new ReadablePathValidator().validate(name, value);

        if (!Files.isRegularFile(value)) {
            throw new ParameterException(
                    "Stien "
                            + value
                            + " peker ikke til en fil, verifiser at du har angitt rett filsti."
            );
        }
    }
}
