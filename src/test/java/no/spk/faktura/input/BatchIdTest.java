package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class BatchIdTest {

    @Test
    void testBatchIdPatternmatch() {
        final Pattern pattern = BatchId.createBatchIdPattern("prefix_");
        assertThat(pattern.matcher("prefix_2015-01-01_01-23-23-21").matches()).isTrue();
    }

    @Test
    void testBatchIdAsTimeIsCorrect() {
        final LocalDateTime timestamp = LocalDateTime.now();
        final BatchId batchId = new BatchId("prefix_", timestamp);
        assertThat(batchId.asLocalDateTime()).isEqualTo(timestamp);
    }

    @Test
    void testBatchIdFromStringIsCorrect() {
        final BatchId batchId = BatchId.fromString("prefix_", "prefix_2015-01-01_02-02-02-00");
        assertThat(batchId.asLocalDateTime()).isEqualTo(LocalDateTime.of(2015, Month.JANUARY, 1, 2, 2, 2));
    }

    @Test
    void testBatchIdToStringReturnsId() {
        final String expected = "prefix_2015-01-01_02-02-02-00";
        final BatchId batchId = BatchId.fromString("prefix_", expected);
        assertThat(batchId.toString()).isEqualTo(expected);
    }

    @Test
    void testBatchIdEqualsHashcodeUsesIdOnly() {
        final String expected = "prefix_2015-01-01_02-02-02-00";
        final BatchId batchId1 = BatchId.fromString("prefix_", expected);
        final BatchId batchId2 = BatchId.fromString("prefix_", expected);
        assertThat(batchId1).isEqualTo(batchId2);
        assertThat(batchId1).hasSameHashCodeAs(batchId2);
    }

    @Test
    void testBatchIdToArbeidskatalogReturnsath() {
        final String expected = "prefix_2015-01-01_02-02-02-00";
        final BatchId batchId1 = BatchId.fromString("prefix_", expected);
        final Path path = batchId1.tilArbeidskatalog(Paths.get("."));
        assertThat(path.toString()).endsWith(expected);
    }
}
