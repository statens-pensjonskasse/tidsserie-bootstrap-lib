package no.spk.faktura.input;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Optional;

import com.beust.jcommander.Parameter;

/**
 * @author Snorre E. Brekke - Computas
 */
public class TestParameters implements Arguments {
    @Parameter(names = { "-h" }, help = true,
            description = "Printer denne oversikten.")
    boolean hjelp;

    @Parameter(names = { "-r" },
            description = "Påkrevd",
            required = true)
    String required;

    @Parameter(names = { "-o" },
            description = "Valgfri")
    private String privateOptional;

    @Parameter(
            names = { "-url" },
            description = "Optional url",
            converter = OptionalStringConverter.class,
            validateWith = JdbcUrlValidator.class
    )
    Optional<String> url = Optional.empty();

    @Parameter(
            names = { "-pw" },
            description = "Optional fil",
            converter = OptionalPathConverter.class,
            validateValueWith = PathValidator.class
    )
    Optional<Path> password = Optional.empty();


    @Parameter(names = "-d",
            description = "Duration",
            validateWith = DurationValidator.class)
    String duration = "0200";

    @Parameter(names = { "-t" },
            description = "Localtime",
            validateWith = LocalTimeValidator.class,
            converter = LocalTimeConverter.class)
    LocalTime time = LocalTime.parse("23:59");

    @Override
    public boolean hjelp() {
        return hjelp;
    }

    public String getPrivateOptional() {
        return privateOptional;
    }

    public void setPrivateOptional(String privateOptional) {
        this.privateOptional = privateOptional;
    }
}
