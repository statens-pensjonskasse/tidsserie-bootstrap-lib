package no.spk.tidsserie.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JdbcPropertiesTest {

    @Test
    void skal_godta_mssql_url() {
        final String navn = "navn";
        final String url = "jdbc:sqlserver://SERVER1:PORT2;databaseName=DATABASE3";
        final String brukeravn = "brukeravn";
        final String passord = "passord";
        final JdbcProperties properties = new JdbcProperties(navn, url, brukeravn, passord);
        assertThat(properties.url()).isEqualTo(url + ";encrypt=true;trustServerCertificate=true;ApplicationName=navn");
        assertThat(properties.brukernavn()).isEqualTo(brukeravn);
        assertThat(properties.passord()).isEqualTo(passord);
        assertThat(properties.server()).isEqualTo("SERVER1");
        assertThat(properties.port()).isEqualTo("PORT2");
        assertThat(properties.database()).isEqualTo("DATABASE3");
    }

    @Test
    void skal_godta_mssql_url_uten_port() {
        final String navn = "navn";
        final String url = "jdbc:sqlserver://SERVER1;databaseName=DATABASE3";
        final String brukeravn = "brukeravn";
        final String passord = "passord";
        final JdbcProperties properties = new JdbcProperties(navn, url, brukeravn, passord);
        assertThat(properties.url()).isEqualTo(url + ";encrypt=true;trustServerCertificate=true;ApplicationName=navn");
        assertThat(properties.brukernavn()).isEqualTo(brukeravn);
        assertThat(properties.passord()).isEqualTo(passord);
        assertThat(properties.server()).isEqualTo("SERVER1");
        assertThat(properties.port()).isEqualTo("");
        assertThat(properties.database()).isEqualTo("DATABASE3");
    }

    @Test
    void testUgyldigJdbcUrlFeiler() {
        final String url = "ulovlig-url";

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new JdbcProperties("navn", url, "brukeravn", "passord"));
        assertTrue(exception.getMessage().contains("ulovlig-url"));
    }
}
