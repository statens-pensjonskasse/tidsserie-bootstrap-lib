package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Snorre E. Brekke - Computas
 */
public class JdbcPropertiesTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void skal_godta_sybase_url() {
        final String navn = "navn";
        final String url = "jdbc:sqlserver://SERVER1:PORT2;database=DATABASE3";
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
        final String url = "jdbc:sqlserver://SERVER1:PORT2;database=DATABASE3";
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
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("ulovlig-url");
        new JdbcProperties("navn", url, "brukeravn", "passord");
    }
}