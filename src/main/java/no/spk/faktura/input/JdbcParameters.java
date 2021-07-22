package no.spk.faktura.input;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * API for å hente ut jdbc-parametere fra program-argumenter angitt fra kommandolinjen.
 *
 * @see JdbcParametersDelegate - kan brukes for å komponere JdbcParameters for {@link ProgramArgumentsFactory}-object
 * @see ProgramArgumentsFactory
 * @see Arguments
 */
public interface JdbcParameters {
    /**
     * @return jdbc-url angitt fra kommandolinja, {@link Optional#empty()} dersom den ikke er angitt.
     */
    Optional<String> getJdbcUrl();

    /**
     * @return jdbc-url angitt fra kommandolinja, {@link Optional#empty()} dersom den ikke er angitt.
     */
    Optional<Path> getJdbcPassordfil();

    /**
     * @return jdbc-url angitt fra kommandolinja, {@link Optional#empty()} dersom den ikke er angitt.
     */
    Optional<String> getJdbcBrukernavn();

    /**
     * Alle paramerterne må være angitt for at Jdbc-config skal fungere. Denne metoden brukes av
     * {@link JdbcParametersPostValidation} for å see om alle eller ingen av parameterene er angitt.
     *
     * @return en strøm av {@link #getJdbcUrl()}, {@link #getJdbcPassordfil()} og {@link #getJdbcBrukernavn()}
     */
    Stream<Optional<?>> jdbcParameters();
}
