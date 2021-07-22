package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Pattern;

import org.junit.Test;

public class BatchIdTest {

    @Test
    public void testBatchIdPatternmatch() {
        final Pattern pattern = BatchId.createBatchIdPattern("prefix_");
        assertThat(pattern.matcher("prefix_2015-01-01_01-23-23-21").matches()).isTrue();
    }

    @Test
    public void testBatchIdAsTimeIsCorrect() {
        final LocalDateTime timestamp = LocalDateTime.now();
        BatchId batchId = new BatchId("prefix_", timestamp);
        assertThat(batchId.asLocalDateTime()).isEqualTo(timestamp);
    }

    @Test
    public void testBatchIdFromStringIsCorrect() {
        final BatchId batchId = BatchId.fromString("prefix_", "prefix_2015-01-01_02-02-02-00");
        assertThat(batchId.asLocalDateTime()).isEqualTo(LocalDateTime.of(2015, Month.JANUARY, 1, 2, 2, 2));
    }

    @Test
    public void testBatchIdToStringReturnsId() {
        final String expected = "prefix_2015-01-01_02-02-02-00";
        final BatchId batchId = BatchId.fromString("prefix_", expected);
        assertThat(batchId.toString()).isEqualTo(expected);
    }

    @Test
    public void testBatchIdEqualsHashcodeUsesIdOnly() {
        final String expected = "prefix_2015-01-01_02-02-02-00";
        final BatchId batchId1 = BatchId.fromString("prefix_", expected);
        final BatchId batchId2 = BatchId.fromString("prefix_", expected);
        assertThat(batchId1).isEqualTo(batchId2);
        assertThat(batchId1.hashCode()).isEqualTo(batchId2.hashCode());
    }

    @Test
    public void testBatchIdToArbeidskatalogReturnsath() {
        final String expected = "prefix_2015-01-01_02-02-02-00";
        final BatchId batchId1 = BatchId.fromString("prefix_", expected);
        final Path path = batchId1.tilArbeidskatalog(Paths.get("."));
        assertThat(path.toString()).endsWith(expected);
    }
}
