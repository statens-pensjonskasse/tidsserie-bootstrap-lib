package no.spk.faktura.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Snorre E. Brekke - Computas
 */
public class PassordfilLeser {
    /**
     * Lese inn passordet frå den angitte passordfila.
     * <p>
     * Linjer som startar med # blir ignorert og whitespace før og etter passordet, inkludert linjeskift,
     * blir også ignorert.
     * <p>
     * Om fila inneheld fleire linjer blir alt anna enn første linje ignorert.
     * <p>
     * NB: Vær varsom med å eksponere passordverdien, det må ikkje eksponerast via loggar eller feilmeldingar seinare i
     * batchen. Unngå kall til denne metoda frå anna enn testcase eller internt i profilen.
     *
     * @param path stien til fila som inneheld passordet som skal lesast inn
     * @return passordet frå passordfila.
     */
    public static String readPassword(final Path path) {
        try {
            return passordLinje(path)
                    .orElseThrow(
                            () -> new IllegalArgumentException(
                                    "Passordfila "
                                            + path
                                            + " er tom eller inneheld kun kommentarlinjer"
                            )
                    );
        } catch (final IOException e) {
            throw new IllegalArgumentException(
                    "Lesing av passordfilen "
                            + path.toAbsolutePath()
                            + " feilet, verifiser at den er lesbar for brukeren/gruppen batchen kjører som"
            );
        }
    }

    private static Optional<String> passordLinje(final Path path) throws IOException {
        return Files.readAllLines(path)
                .stream()
                .map(String::trim)
                .filter(linje -> !linje.startsWith("#"))
                .findFirst();
    }
}
