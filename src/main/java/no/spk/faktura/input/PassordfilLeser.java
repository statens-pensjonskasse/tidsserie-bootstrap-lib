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
     * Lese inn passordet fr� den angitte passordfila.
     * <p>
     * Linjer som startar med # blir ignorert og whitespace f�r og etter passordet, inkludert linjeskift,
     * blir ogs� ignorert.
     * <p>
     * Om fila inneheld fleire linjer blir alt anna enn f�rste linje ignorert.
     * <p>
     * NB: V�r varsom med � eksponere passordverdien, det m� ikkje eksponerast via loggar eller feilmeldingar seinare i
     * batchen. Unng� kall til denne metoda fr� anna enn testcase eller internt i profilen.
     *
     * @param path stien til fila som inneheld passordet som skal lesast inn
     * @return passordet fr� passordfila.
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
                            + " feilet, verifiser at den er lesbar for brukeren/gruppen batchen kj�rer som"
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
