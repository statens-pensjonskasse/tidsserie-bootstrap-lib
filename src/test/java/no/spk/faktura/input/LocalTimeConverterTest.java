package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class LocalTimeConverterTest {

    private LocalTimeConverter converter;

    @Before
    public void setUp() {
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
        assertThrows(DateTimeException.class, () -> converter.convert("3:00"));
    }

    @Test
    public void testOneDigitHourWithoutColonFails() {
        assertThrows(DateTimeException.class, () -> converter.convert("300"));
    }
}
