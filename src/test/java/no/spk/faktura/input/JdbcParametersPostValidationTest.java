package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import java.io.IOException;

import static java.util.Optional.of;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class JdbcParametersPostValidationTest {

    @Rule
    public final TestName name = new TestName();

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    private JdbcParametersPostValidation validator;

    private JdbcParametersDelegate arguments;

    @Before
    public void _before() {
        arguments = new JdbcParametersDelegate();
        validator = new JdbcParametersPostValidation();
    }
    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    public void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCURLErAngitt() {
        arguments.jdbcUrl = (of("jdbc:jtds:sybase:whatnot"));

        ParameterException parameterException = assertThrows(ParameterException.class, () -> valider(arguments));
        assertTrue(parameterException.getMessage().contains(JdbcParametersPostValidation.feilmelding()));
    }

    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    public void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCBrukernavnErAngitt() {
        arguments.jdbcBrukernavn = of("myself");

        ParameterException parameterException = assertThrows(ParameterException.class, () -> valider(arguments));
        assertTrue(parameterException.getMessage().contains(JdbcParametersPostValidation.feilmelding()));
    }

    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    public void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCPassordfilErAngitt() throws IOException {
        arguments.jdbcPassordfil = of(temp.newFile(name.getMethodName()).toPath());

        ParameterException parameterException = assertThrows(ParameterException.class, () -> valider(arguments));
        assertTrue(parameterException.getMessage().contains(JdbcParametersPostValidation.feilmelding()));
    }

    private void valider(final JdbcParameters programArguments) {
        validator.validate(programArguments, dummySpec());
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
