package no.spk.faktura.input;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class JdbcUrlValidatorTest {

    @Rule
    public final ExpectedException e = ExpectedException.none();

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
        e.expect(ParameterException.class);
        final String parameterNavn = "yadayada";
        e.expectMessage(parameterNavn);
        e.expectMessage(" må inneholde en gyldig JDBC-url på formen 'jdbc:jtds:sybase://<server>:<port>/<database>'");

        new JdbcUrlValidator().validate(parameterNavn, "jdbc:jtds:sybase://syb08t.spk.no:4100/CI_TRUNK;appname=tt", dummySpec());
    }

    @Test
    public void skalAvviseUrlMedApplikjasjonsnavn() {
        e.expect(ParameterException.class);
        final String parameterNavn = "yadayada";
        e.expectMessage(parameterNavn);
        e.expectMessage(" må inneholde en gyldig JDBC-url på formen 'jdbc:jtds:sybase://<server>:<port>/<database>'");

        new JdbcUrlValidator().validate(parameterNavn, "jdbc:jtds:", dummySpec());
    }

    @Test
    public void skalAvviseFullstendigRubsih() {
        e.expect(ParameterException.class);

        new JdbcUrlValidator().validate("jdbcUrl", "wth?", dummySpec());
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
