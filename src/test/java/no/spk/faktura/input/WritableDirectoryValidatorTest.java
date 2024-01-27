package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class WritableDirectoryValidatorTest {

    @TempDir
    Path temp;

    private final WritableDirectoryValidator validator = new WritableDirectoryValidator();

    @Test
    void testManglendeStiFeiler() {
        final ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", Paths.get("nowhere"), dummySpec()));
        assertTrue(exception.getMessage().contains("eksisterer ikke"));
    }

    @Test
    void testStiErFilFeiler(final TestInfo testInfo) throws IOException {
        final File file = Files.createFile(temp.resolve(testInfo.getDisplayName())).toFile();
        final ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", file.toPath(), dummySpec()));
        assertTrue(exception.getMessage().contains("peker ikke til en katalog"));
    }

    @Test
    void testStiIkkeLesbarFeiler(final TestInfo testInfo) throws IOException {
        assumeFalse(isWindowsOs());
        final File file = Files.createDirectories(temp.resolve(testInfo.getDisplayName())).toFile();
        assertThat(file.setReadable(false)).isTrue();

        final ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", file.toPath(), dummySpec()));
        assertTrue(exception.getMessage().contains("er ikke lesbar for batchen"));
        assertThat(file.setReadable(true)).isTrue();
    }

    @Test
    void testStiIkkeSkrivbarFeiler() {
        assumeFalse(isWindowsOs());
        final File file = new File("/");

        final ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", file.toPath(), dummySpec()));
        assertTrue(exception.getMessage().contains("er ikke skrivbar for batchen"));
    }

    private boolean isWindowsOs() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
