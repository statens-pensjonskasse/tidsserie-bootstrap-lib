package no.spk.faktura.input;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class PathStringValidator {
    public void validate(final String name, final String value) throws ParameterException {
        try {
            Paths.get(value);
        } catch (InvalidPathException e) {
            throw new ParameterException(
                    new CommandLine(new DummyCommand()),
                    name + " er ikke en gyldig filbane: " + value,
                    e
            );
        }
    }
}
