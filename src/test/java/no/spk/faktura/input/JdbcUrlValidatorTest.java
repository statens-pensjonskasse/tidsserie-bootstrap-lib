package no.spk.faktura.input;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class JdbcUrlValidatorTest {

    /**
     * Ok, negativ test av alle mulige feil verdiar er ikkje realistisk men la oss no iallefall
     * verifisere at URLen vi oftast forventar å støte på blir godtatt av valideringa.
     */
    @Test
    void skalGodtaJdbcUrlMedDatabaseName() {
        new JdbcUrlValidator().validate("jdbcUrl", "jdbc:sqlserver://syb123:4100;databaseName=kasper123", dummySpec());
    }

    @Test
    void skalGodtaJdbcUrlMedDatabase() {
        new JdbcUrlValidator().validate("jdbcUrl", "jdbc:sqlserver://syb123:4100;database=kasper123", dummySpec());
    }

    @Test
    void skalGodtaJdbcUrlUtenPortnummer() {
        new JdbcUrlValidator().validate("jdbcUrl", "jdbc:sqlserver://syb123;databaseName=kasper123", dummySpec());
    }

    @Test
    void skalGodtaJtdsJdbcUrlMedPunktum() {
        new JdbcUrlValidator().validate("jdbcUrl", "jdbc:sqlserver://syb08t.spk.no:4100;databaseName=CI_TRUNK", dummySpec());
    }

    @Test
    void skalAvviseUfullstendigUrl() {
        final String parameterNavn = "yadayada";
        final ParameterException exception = assertThrows(ParameterException.class, () -> {
            new JdbcUrlValidator().validate(parameterNavn, "jdbc:sqlserver://", dummySpec());
        });
        assertTrue(exception.getMessage().contains(parameterNavn));
        assertTrue(exception.getMessage().contains(" må inneholde en gyldig JDBC-url på formen 'jdbc:sqlserver://<server>:<port>;database=<database>' " +
                "eller 'jdbc:sqlserver://<server>:<port>;databaseName=<database>', eventuelt uten portnummer"));
    }

    @Test
    void skalAvviseUrlMedApplikjasjonsnavn() {
        final String parameterNavn = "yadayada";
        final ParameterException exception = assertThrows(ParameterException.class, () -> {
            new JdbcUrlValidator().validate(parameterNavn, "jdbc:sqlserver://syb08t.spk.no:4100;databaseName=CI_TRUNK;ApplicationName=tt", dummySpec());
        });
        assertTrue(exception.getMessage().contains(parameterNavn));
        assertTrue(exception.getMessage().contains(" må inneholde en gyldig JDBC-url på formen 'jdbc:sqlserver://<server>:<port>;database=<database>' " +
                "eller 'jdbc:sqlserver://<server>:<port>;databaseName=<database>', eventuelt uten portnummer"));
    }

    @Test
    void skalAvviseFullstendigRubbish() {
        assertThrows(ParameterException.class, () -> new JdbcUrlValidator().validate("jdbcUrl", "wth?", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
