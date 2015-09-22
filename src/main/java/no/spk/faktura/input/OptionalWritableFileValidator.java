package no.spk.faktura.input;

import java.nio.file.Path;
import java.util.Optional;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class OptionalWritableFileValidator implements IValueValidator<Optional<Path>> {
    @Override
    public void validate(final String name, final Optional<Path> value) throws ParameterException {
        if (!value.isPresent()) {
            return;
        }
        new WritableFileValidator().validate(name, value.get());
    }
}
