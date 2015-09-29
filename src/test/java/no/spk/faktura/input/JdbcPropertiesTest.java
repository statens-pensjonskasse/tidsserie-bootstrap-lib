package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Snorre E. Brekke - Computas
 */
public class JdbcPropertiesTest {

    @Test
    public void testPropertiesBlirSatt() throws Exception {
        final String navn = "navn";
        final String url = "url";
        final String brukeravn = "brukeravn";
        final String passord = "passord";
        JdbcProperties properties = new JdbcProperties(navn, url, brukeravn, passord);
        assertThat(properties.url()).isEqualTo("url;appName=navn");
        assertThat(properties.brukernavn()).isEqualTo(brukeravn);
        assertThat(properties.passord()).isEqualTo(passord);
    }
}