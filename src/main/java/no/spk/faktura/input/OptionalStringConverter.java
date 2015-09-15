package no.spk.faktura.input;

import java.util.Optional;

import com.beust.jcommander.IStringConverter;

public class OptionalStringConverter implements IStringConverter<Optional<String>> {
    @Override
    public Optional<String> convert(final String s) {
        return Optional.ofNullable(s);
    }
}
