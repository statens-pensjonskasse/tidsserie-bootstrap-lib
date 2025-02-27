package no.spk.tidsserie.input;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import picocli.CommandLine.ITypeConverter;

public class OptionalPathConverter implements ITypeConverter<Optional<Path>> {
    @Override
    public Optional<Path> convert(final String s) {
        return Optional.ofNullable(s).map(Paths::get);
    }
}
