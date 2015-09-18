package no.spk.faktura.input;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * API for � hente ut jdbc-parametere fra program-argumenter angitt fra kommandolinjen.
 * @author Snorre E. Brekke - Computas
 * @see JdbcParametersDelegate - kan brukes for � komponere JdbcParameters for {@�link ProgramArgumentsFactory}-object
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
     * Alle paramerterne m� v�re angitt for att Jdbc-config skal fungere. Denne metoden brukes av
     * {@link JdbcParametersPostValidation} for � see om alle eller ingen av parameterene er angitt.
     * @return en str�m av {@link #getJdbcUrl()}, {@link #getJdbcPassordfil()} og {@link #getJdbcBrukernavn()}
     */
    Stream<Optional<?>> jdbcParameters();
}
