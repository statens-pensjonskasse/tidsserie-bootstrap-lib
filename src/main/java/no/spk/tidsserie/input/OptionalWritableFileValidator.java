package no.spk.tidsserie.input;

import java.nio.file.Path;
import java.util.Optional;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class OptionalWritableFileValidator {

    public void validate(final String name, final Optional<Path> value, final CommandSpec spec) throws ParameterException {
        if (!value.isPresent()) {
            return;
        }
        new WritableFileValidator().validate(name, value.get(), spec);
    }
}
