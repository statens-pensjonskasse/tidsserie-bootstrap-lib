package no.spk.faktura.input;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class JdbcParametersPostValidationTest {

    @TempDir
    Path temp;

    private JdbcParametersPostValidation validator;

    private JdbcParametersDelegate arguments;

    @BeforeEach
    void _before() {
        arguments = new JdbcParametersDelegate();
        validator = new JdbcParametersPostValidation();
    }

    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCURLErAngitt() {
        arguments.jdbcUrl = (of("jdbc:jtds:sybase:whatnot"));

        final ParameterException parameterException = assertThrows(ParameterException.class, () -> valider(arguments));
        assertTrue(parameterException.getMessage().contains(JdbcParametersPostValidation.feilmelding()));
    }

    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCBrukernavnErAngitt() {
        arguments.jdbcBrukernavn = of("myself");

        final ParameterException parameterException = assertThrows(ParameterException.class, () -> valider(arguments));
        assertTrue(parameterException.getMessage().contains(JdbcParametersPostValidation.feilmelding()));
    }

    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCPassordfilErAngitt(final TestInfo testInfo) throws IOException {
        arguments.jdbcPassordfil = of(Files.createFile(temp.resolve(testInfo.getDisplayName())));

        final ParameterException parameterException = assertThrows(ParameterException.class, () -> valider(arguments));
        assertTrue(parameterException.getMessage().contains(JdbcParametersPostValidation.feilmelding()));
    }

    private void valider(final JdbcParameters programArguments) {
        validator.validate(programArguments, dummySpec());
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
