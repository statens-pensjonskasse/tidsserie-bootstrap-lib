package no.spk.faktura.input;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class JdbcPropertiesTest {

    @Test
    public void skal_godta_sybase_url() {
        final String navn = "navn";
        final String url = "jdbc:jtds:sybase://SERVER1:PORT2/DATABASE3";
        final String brukeravn = "brukeravn";
        final String passord = "passord";
        JdbcProperties properties = new JdbcProperties(navn, url, brukeravn, passord);
        assertThat(properties.url()).isEqualTo(url + ";appName=navn");
        assertThat(properties.brukernavn()).isEqualTo(brukeravn);
        assertThat(properties.passord()).isEqualTo(passord);
        assertThat(properties.server()).isEqualTo("SERVER1");
        assertThat(properties.port()).isEqualTo("PORT2");
        assertThat(properties.database()).isEqualTo("DATABASE3");
    }

    @Test
    public void skal_godta_mssql_url() {
        final String navn = "navn";
        final String url = "jdbc:jtds:sqlserver://SERVER1:PORT2/DATABASE3";
        final String brukeravn = "brukeravn";
        final String passord = "passord";
        JdbcProperties properties = new JdbcProperties(navn, url, brukeravn, passord);
        assertThat(properties.url()).isEqualTo(url + ";appName=navn");
        assertThat(properties.brukernavn()).isEqualTo(brukeravn);
        assertThat(properties.passord()).isEqualTo(passord);
        assertThat(properties.server()).isEqualTo("SERVER1");
        assertThat(properties.port()).isEqualTo("PORT2");
        assertThat(properties.database()).isEqualTo("DATABASE3");
    }

    @Test
    public void testUgyldigJdbcUrlFeiler() {
        final String url = "ulovlig-url";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new JdbcProperties("navn", url, "brukeravn", "passord"));
        assertTrue(exception.getMessage().contains( "ulovlig-url"));
    }
}
