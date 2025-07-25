package no.spk.tidsserie.input;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Optional;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "tests", mixinStandardHelpOptions = true, version = "0.1")
public class TestParameters implements Arguments {

    @Spec
    CommandSpec spec;

    String duration;
    Optional<String> url = Optional.empty();
    LocalTime time = LocalTime.parse("23:59");
    Optional<Path> password = Optional.empty();

    @Option(
            names = { "-r" },
            description = "Påkrevd",
            required = true
    )
    String required;

    @Option(
            names = "-d",
            description = "Duration"
    )
    public void setDuration(final String value) {
        new DurationValidator().validate("kjøretid", value, spec);
        duration = value;
    }

    @Option(
            names = {"-url"},
            description = "Optional url"
    )
    public void setUrl(final String value) {
        new JdbcUrlValidator().validate("url", value, spec);
        url = new OptionalStringConverter().convert(value);
    }

    @Option(names = {"-t"},
            description = "Localtime"
    )
    public void setTime(final String value) {
        new LocalTimeValidator().validate("time", value, spec);
        time = new LocalTimeConverter().convert(value);
    }

    @Option(
            names = {"-pw"},
            description = "Optional fil"
    )
    public void setPassword(final String value) {
        final Optional<Path> path = new OptionalPathConverter().convert(value);
        new PathValidator().validate("password", path, spec);
        password = path;
    }

    @Override
    public boolean hjelp() {
        return false;
    }
}
