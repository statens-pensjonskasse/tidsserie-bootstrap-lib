package no.spk.faktura.input;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

/**
 * Kompositt-klasse som kan brukes ved implementering av  {@link Arguments} til bruk
 * i {@link ProgramArgumentsFactory}.
 * Annoter kompositt-feltet med {@link ParametersDelegate}.
 * <br>
 * Eks:
 * <pre>
 * <code>
 * public class MyArguments implements Arguments, JdbcParameters{
 *     {@literal @}ParametersDelegate
 *      JdbcParametersDelegate jdbcParams = new JdbcParametersDelegate();
 *
 *      (...)
 *  }
 *  </code>
 * </pre>
 *
 * @author Snorre E. Brekke - Computas
 */
public class JdbcParametersDelegate implements JdbcParameters {
    @Parameter(
            names = { "-jdbcUrl" },
            description = "JDBC URL for databasetilkoblingen som batchen skal bruke.",
            converter = OptionalStringConverter.class,
            validateWith = JdbcUrlValidator.class
    )
    Optional<String> jdbcUrl = Optional.empty();

    @Parameter(
            names = { "-jdbcPassordfil" },
            description = "Stien til filen som inneholder passordet for databasetilkoblingen",
            converter = OptionalPathConverter.class,
            validateValueWith = PathValidator.class
    )
    Optional<Path> jdbcPassordfil = Optional.empty();

    @Parameter(
            names = { "-jdbcBrukernavn" },
            description = "Brukernavnet for databasetilkoblingen",
            converter = OptionalStringConverter.class
    )
    Optional<String> jdbcBrukernavn = Optional.empty();

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

    public void setJdbcUrl(Optional<String> jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setJdbcPassordfil(Optional<Path> jdbcPassordfil) {
        this.jdbcPassordfil = jdbcPassordfil;
    }

    public void setJdbcBrukernavn(Optional<String> jdbcBrukernavn) {
        this.jdbcBrukernavn = jdbcBrukernavn;
    }
}
