package no.spk.faktura.input;

import static java.util.Optional.of;

import java.io.IOException;

import com.beust.jcommander.ParameterException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

/**
 * @author Snorre E. Brekke - Computas
 */
public class JdbcParametersPostValidationTest {

    @Rule
    public final ExpectedException e = ExpectedException.none();

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
        e.expect(ParameterException.class);
        e.expectMessage(JdbcParametersPostValidation.feilmelding());

        arguments.jdbcUrl = (of("jdbc:jtds:sybase:whatnot"));

        valider(arguments);
    }

    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    public void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCBrukernavnErAngitt() {
        e.expect(ParameterException.class);
        e.expectMessage(JdbcParametersPostValidation.feilmelding());

        arguments.jdbcBrukernavn = of("myself");

        valider(arguments);
    }

    /**
     * Verifiserer at alle eller ingen av JDBC-argumenta til batchen har ein verdi ettersom vi automatisk byttar
     * profil til kommandolinjeprofil når eit av dei er satt.
     */
    @Test
    public void skalKreveAtAlleJDBCParameterHarEnVerdiHvisJDBCPassordfilErAngitt() throws IOException {
        e.expect(ParameterException.class);
        e.expectMessage(JdbcParametersPostValidation.feilmelding());

        arguments.jdbcPassordfil = of(temp.newFile(name.getMethodName()).toPath());

        valider(arguments);
    }

    private void valider(JdbcParameters programArguments) {
        validator.validate(programArguments);
    }
}