package no.spk.tidsserie.input;

import java.util.Optional;

import picocli.CommandLine.ITypeConverter;

public class OptionalStringConverter implements ITypeConverter<Optional<String>> {
    @Override
    public Optional<String> convert(final String s) {
        return Optional.ofNullable(s);
    }
}
