package no.spk.tidsserie.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PassordfilLeserTest {

    @TempDir
    Path folder;

    @Test
    void skalTrimmeAllWhitespaceRundtPassordet() throws IOException {
        final Path fil = passordfil();
        writePassword(fil, "       I like my spaces      ");
        assertThat(PassordfilLeser.readPassword(fil)).isEqualTo("I like my spaces");
    }

    @Test
    void skalIgnorereKommentarLinjer() throws IOException {
        final Path fil = passordfil();
        writePassword(fil, "# This is my password\nMy secret password\n");
        assertThat(PassordfilLeser.readPassword(fil)).isEqualTo("My secret password");
    }

    /**
     * Verifiserer at passordet blir henta frå første linje i passordfila som ikkje er kommentarlinjer.
     */
    @Test
    void skalHentePassordFrafoersteInnholdslinje() throws IOException {
        final Path fil = passordfil();
        writePassword(fil, "# This is my password\nMy supersecret password\nMy other secret password");
        assertThat(PassordfilLeser.readPassword(fil)).isEqualTo("My supersecret password");
    }

    private Path passordfil() throws IOException {
        return Files.createFile(folder.resolve("topsecret"));
    }

    private void writePassword(Path file, String expected) throws IOException {
        Files.write(file, expected.getBytes());
    }
}
