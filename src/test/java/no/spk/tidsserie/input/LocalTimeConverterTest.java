package no.spk.tidsserie.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalTimeConverterTest {

    private LocalTimeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LocalTimeConverter();
    }

    @Test
    void testLegalConversions() {
        assertThat(converter.convert("23:00")).isEqualTo(LocalTime.of(23, 0));
        assertThat(converter.convert("2300")).isEqualTo(LocalTime.of(23, 0));
        assertThat(converter.convert("23:00:00")).isEqualTo(LocalTime.of(23, 0, 0));
        assertThat(converter.convert("230000")).isEqualTo(LocalTime.of(23, 0, 0));
    }

    @Test
    void testOneDigitHourFails() {
        assertThrows(DateTimeException.class, () -> converter.convert("3:00"));
    }

    @Test
    void testOneDigitHourWithoutColonFails() {
        assertThrows(DateTimeException.class, () -> converter.convert("300"));
    }
}
