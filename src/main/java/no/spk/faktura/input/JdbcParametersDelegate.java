package no.spk.faktura.input;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Kompositt-klasse som kan brukes ved implementering av {@link Arguments} til bruk
 * i {@link ProgramArgumentsFactory}.
 * Annoter kompositt-feltet med {@link CommandLine.Mixin}.
 * <br>
 * Eks:
 * <pre>
 * <code>
 * public class MyArguments implements Arguments, JdbcParameters {
 *     {@literal @}Mixin
 *      JdbcParametersDelegate jdbcParams = new JdbcParametersDelegate();
 *
 *      (...)
 *  }
 *  </code>
 * </pre>
 */
public class JdbcParametersDelegate implements JdbcParameters {

    Optional<Path> jdbcPassordfil = Optional.empty();
    Optional<String> jdbcUrl = Optional.empty();
    Optional<String> jdbcBrukernavn = Optional.empty();

    @Option(
            names = { "-jdbcUrl" },
            description = "JDBC URL for databasetilkoblingen som batchen skal bruke."
    )
    public void setJdbcUrl(final String value) {
        new JdbcUrlValidator().validate("-jdbcUrl", value);
        jdbcUrl = new OptionalStringConverter().convert(value);
    }

    @Option(
            names = { "-jdbcPassordfil" },
            description = "Stien til filen som inneholder passordet for databasetilkoblingen"
    )
    public void setJdbcPassordfil(final String value) {
        final Optional<Path> optionalPath = new OptionalPathConverter().convert(value);
        new PathValidator().validate("-jdbcPassordfil", optionalPath);
        jdbcPassordfil = optionalPath;
    }

    @Option(
            names = { "-jdbcBrukernavn" },
            description = "Brukernavnet for databasetilkoblingen"
    )
    public void setJdbcBrukernavn(final String value) {
        jdbcBrukernavn = new OptionalStringConverter().convert(value);
    }

    @Override
    public Optional<String> getJdbcUrl() {
        return jdbcUrl;
    }

    @Override
    public Optional<Path> getJdbcPassordfil() {
        return jdbcPassordfil;
    }

    @Override
    public Optional<String> getJdbcBrukernavn() {
        return jdbcBrukernavn;
    }

    @Override
    public Stream<Optional<?>> jdbcParameters() {
        return Stream.of(jdbcUrl, jdbcBrukernavn, jdbcPassordfil);
    }
}
