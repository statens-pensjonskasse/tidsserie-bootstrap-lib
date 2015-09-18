package no.spk.faktura.input;

import java.util.Optional;

import com.beust.jcommander.ParameterException;

/**
 * Brukes for konsistenssjekking av JdbcParameters. Sjekker at JdbcParameters inneholder alle eller ingen av feltene angitt.
 * @author Snorre E. Brekke - Computas
 */
public class JdbcParametersPostValidation {
    /**
     * Sjekker at JdbcParameters inneholder alle eller ingen av feltene angitt.
     * @param parameters kommandolinjeargument som skal sjekkes for konsistens
     * @throws ParameterException dersom bare deler (en eller to) av jdbcUrl, jdbcPassordfil og jdbcBruker er angitt.
     */
    public void validate(final JdbcParameters parameters) throws ParameterException {
        if (!ingenDatabaseArgumenterAngitt(parameters) && !alleDatabaseArgumenterAngitt(parameters)) {
            throw new ParameterException(feilmelding());
        }
    }

    static String feilmelding() {
        return "Både -jdbcUrl, -jdbcBrukernavn og -jdbcPassordfil må ha en verdi dersom et eller flere av parameterne har fått angitt en verdi";
    }

    private boolean alleDatabaseArgumenterAngitt(final JdbcParameters programArguments) {
        return programArguments.jdbcParameters().allMatch(Optional::isPresent);
    }

    private boolean ingenDatabaseArgumenterAngitt(final JdbcParameters programArguments) {
        return programArguments.jdbcParameters().noneMatch(Optional::isPresent);
    }
}
