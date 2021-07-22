package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DateTimeException;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LocalTimeConverterTest {

    private LocalTimeConverter converter;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        converter = new LocalTimeConverter();
    }

    @Test
    public void testLegalConversions() {
        assertThat(converter.convert("23:00")).isEqualTo(LocalTime.of(23, 0));
        assertThat(converter.convert("2300")).isEqualTo(LocalTime.of(23, 0));
        assertThat(converter.convert("23:00:00")).isEqualTo(LocalTime.of(23, 0, 0));
        assertThat(converter.convert("230000")).isEqualTo(LocalTime.of(23, 0, 0));
    }

    @Test
    public void testOneDigitHourFails() {
        exception.expect(DateTimeException.class);
        converter.convert("3:00");
    }

    @Test
    public void testOneDigitHourWithoutColonFails() {
        exception.expect(DateTimeException.class);
        converter.convert("300");
    }
}
