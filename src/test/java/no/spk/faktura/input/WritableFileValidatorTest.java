package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

@Ignore
public class WritableFileValidatorTest {

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    @Rule
    public final TestName testName = new TestName();

    WritableFileValidator validator;

    @Before
    public void setUp() {
        validator = new WritableFileValidator();
    }

    @Test
    public void testManglendeStiFeiler() {
        ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", Paths.get("nowhere"), dummySpec()));
        assertTrue(exception.getMessage().contains("eksisterer ikke"));
    }

    @Test
    public void testStiErFilFeiler() throws Exception {
        final File file = temp.newFolder(testName.getMethodName());

        ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", file.toPath(), dummySpec()));
        assertTrue(exception.getMessage().contains("peker ikke til en fil"));
    }

    @Test
    public void testStiIkkeLesbarFeiler() throws Exception {
        assumeFalse(isWindowsOs());
        final File file = temp.newFile(testName.getMethodName());
        assertThat(file.setReadable(false)).isTrue();

        ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", file.toPath(), dummySpec()));
        assertTrue(exception.getMessage().contains("er ikke lesbar for batchen"));

        assertThat(file.setReadable(true)).isTrue();
    }

    @Test
    public void testStiIkkeSkrivbarFeiler() throws Exception {
        assumeFalse(isWindowsOs());
        final File file = temp.newFile(testName.getMethodName());
        assertThat(file.setWritable(false)).isTrue();

        ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate("bane", file.toPath(), dummySpec()));
        assertTrue(exception.getMessage().contains("er ikke skrivbar for batchen"));

        assertThat(file.setWritable(true)).isTrue();
    }

    @Test
    public void testOptionalFilManglerSkalIkkeFeile() {
        new OptionalWritableFileValidator().validate("bane", Optional.empty(), dummySpec());
    }

    private boolean isWindowsOs() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
