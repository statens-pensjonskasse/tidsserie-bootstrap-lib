package no.spk.faktura.input;

import org.junit.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class JdbcUrlValidatorTest {

    /**
     * Ok, negativ test av alle mulige feil verdiar er ikkje realistisk men la oss no iallefall
     * verifisere at URLen vi oftast forventar å støte på blir godtatt av valideringa.
     */
    @Test
    public void skalGodtaJtdsJdbcUrl() {
        new JdbcUrlValidator().validate("jdbcUrl", "jdbc:jtds:sybase://syb123:4100/kasper123", dummySpec());
    }

    @Test
    public void skalGodtaJtdsJdbcUrlMedPunktum() {
        new JdbcUrlValidator().validate("jdbcUrl", "jdbc:jtds:sybase://syb08t.spk.no:4100/CI_TRUNK", dummySpec());
    }

    @Test
    public void skalAvviseUfullstendigUrl() {
        final String parameterNavn = "yadayada";
        ParameterException exception = assertThrows(ParameterException.class, () -> {
            new JdbcUrlValidator().validate(parameterNavn, "jdbc:jtds:sybase://syb08t.spk.no:4100/CI_TRUNK;appname=tt", dummySpec());
        });
        assertTrue(exception.getMessage().contains(parameterNavn));
        assertTrue(exception.getMessage().contains(" må inneholde en gyldig JDBC-url på formen 'jdbc:jtds:sybase://<server>:<port>/<database>'"));
    }

    @Test
    public void skalAvviseUrlMedApplikjasjonsnavn() {
        final String parameterNavn = "yadayada";
        ParameterException exception = assertThrows(ParameterException.class, () -> {
            new JdbcUrlValidator().validate(parameterNavn, "jdbc:jtds:", dummySpec());
        });
        assertTrue(exception.getMessage().contains(parameterNavn));
        assertTrue(exception.getMessage().contains(" må inneholde en gyldig JDBC-url på formen 'jdbc:jtds:sybase://<server>:<port>/<database>'"));
    }

    @Test
    public void skalAvviseFullstendigRubsih() {
        assertThrows(ParameterException.class, () -> new JdbcUrlValidator().validate("jdbcUrl", "wth?", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
