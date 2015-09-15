package no.spk.faktura.input;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * BatchId er en streng basert p� {@link LocalDateTime} ned sekunddeler p� formen <i>batch_yyyy-MM-dd_HH-mm-ss-SS</i>.
 *
 * @author Snorre E. Brekke - Computas
 */
public class BatchId {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SS");

    private final String id;
    private LocalDateTime localDateTime;

    public BatchId(String prefix, LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
        this.id = prefix + dateTimeFormatter.format(localDateTime);
    }

    public static Pattern createBatchIdPattern(String batchIdPrefix) {
        return Pattern.compile("^(" + batchIdPrefix + "\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}-\\d{2})$");
    }

    /**
     * Gir LocalDateTime som BatchId er generert utifra.
     * <p><b>Merk:</b> dersom {@code batchId1.equals(batchId2)} betyr <i>ikke</i> det at {@code batchId1.asLocalDateTime().equals(batchId2.asLocalDateTime())} fordi
     * genereringstidspunktet kan avvike p� millisekundniv�.</p>
     *
     * @return LocalDateTime BatchId er generert utifra.
     */
    public LocalDateTime asLocalDateTime() {
        return localDateTime;
    }

    /**
     * Konverterer en batch-streng til en ny BatchId-instans. Metoden forutsetter at {@code batchId} er p� formatet <i>batch_yyyy-MM-dd_HH-mm-ss-SS</i>.
     *
     * @param prefix prefix p� batchid
     * @param batchId streng p� formatet <i>prefixyyyy-MM-dd_HH-mm-ss-SS</i>.
     * @return ny BatchID-instans basert p� {@code batchId}
     */
    public static BatchId fromString(String prefix, String batchId) {
        return new BatchId(prefix, LocalDateTime.parse(batchId.substring(prefix.length()), dateTimeFormatter));
    }

    @Override
    public String toString() {
        return id;
    }

    /**
     * {@code batchId1.equals(batchId2)} dersom strengene de representerer er like.
     *
     * @param o objektet som skal sammenlignes med denn BatchId-instansen
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BatchId)) return false;

        BatchId batchId = (BatchId) o;

        return id.equals(batchId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Returnerer stien til den unike underkatalogen for batchk�yringa som batchid representerer.
     * <p>
     * Metoda returnerer kun stien til katalogen, veirifsering eller opprettelse av katalogen
     * er det opp til klientane av denne metoda � ta seg av.
     *
     * @param utKatalog batchens ut-katalog, den nye katalogen blir ein underkatalog til denne
     * @return stien til den nye arbeidskatalogen
     */
    public Path tilArbeidskatalog(final Path utKatalog) {
        return Paths.get(
                utKatalog.toString(),
                id
        );
    }
}
