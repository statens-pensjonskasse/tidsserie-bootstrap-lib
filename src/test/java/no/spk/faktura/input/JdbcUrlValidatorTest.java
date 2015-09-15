package no.spk.faktura.input;

import com.beust.jcommander.ParameterException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class JdbcUrlValidatorTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    /**
     * Ok, negativ test av alle mulige feil verdiar er ikkje realistisk men la oss no iallefall
     * verifisere at URLen vi oftast forventar � st�te p� blir godtatt av valideringa.
     */
    @Test
    public void skalGodtaJtdsJdbcUrl() {
        new JdbcUrlValidator().validate("jdbcUrl", "jdbc:jtds:sybase://syb123:4100/kasper123");
    }

    @Test
    public void skalAvviseUfullstendigUrl() {
        e.expect(ParameterException.class);
        final String parameterNavn = "yadayada";
        e.expectMessage(parameterNavn);
        e.expectMessage("m� inneholde en gyldig JDBC-url p� formen 'jdbc:<subprotocol>:<subname>'");

        new JdbcUrlValidator().validate(parameterNavn, "jdbc:jtds:");
    }

    @Test
    public void skalAvviseFullstendigRubsih() {
        e.expect(ParameterException.class);

        new JdbcUrlValidator().validate("jdbcUrl", "wth?");
    }
}