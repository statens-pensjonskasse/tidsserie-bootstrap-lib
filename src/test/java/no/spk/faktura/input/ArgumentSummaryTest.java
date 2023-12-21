package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class ArgumentSummaryTest {

    @Test
    void skal_returnere_en_streng_av_printbare_argumenter() {
        assertThat(
                ArgumentSummary.createParameterSummary(
                        new TestKlasse()
                )
        )
                .isEqualTo(
                        "\n-kjøretid: 123\n-dato: 2020-01-01\n-slett: true\n-stopptid: 777\n\n"
                );
    }

    private static class TestKlasse implements PrintbareProgramargumenter {

        @Override
        public List<String> argumenter() {
            return Arrays.asList(
                    String.format("kjøretid: %d", 123),
                    String.format("dato: %s", LocalDate.of(2020, 1, 1)),
                    String.format("slett: %b", true),
                    String.format("stopptid: %d", 777)
            );
        }
    }
}
