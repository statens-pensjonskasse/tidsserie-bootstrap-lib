package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DateTimeException;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Snorre E. Brekke - Computas
 */
public class LocalTimeConverterTest {

    private LocalTimeConverter converter;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        converter = new LocalTimeConverter();
    }

    @Test
    public void testLegalConversions() throws Exception {
        assertThat(converter.convert("23:00")).isEqualTo(LocalTime.of(23, 0));
        assertThat(converter.convert("23:00:00")).isEqualTo(LocalTime.of(23, 0, 0));
    }

    @Test
    public void testillegalLegalConversions() throws Exception {
        exception.expect(DateTimeException.class);
        converter.convert("3:00");
    }
}