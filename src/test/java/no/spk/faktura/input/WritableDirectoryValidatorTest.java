package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeFalse;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import picocli.CommandLine.ParameterException;

public class WritableDirectoryValidatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    @Rule
    public final TestName testName = new TestName();

    WritableDirectoryValidator validator;

    @Before
    public void setUp() {
        validator = new WritableDirectoryValidator();
    }

    @Test
    public void testManglendeStiFeiler() {
        exception.expect(ParameterException.class);
        exception.expectMessage("eksisterer ikke");
        validator.validate("bane", Paths.get("nowhere"));
    }

    @Test
    public void testStiErFilFeiler() throws Exception {
        final File file = temp.newFile(testName.getMethodName());
        exception.expect(ParameterException.class);
        exception.expectMessage("peker ikke til en katalog");
        validator.validate("bane", file.toPath());
    }

    @Test
    public void testStiIkkeLesbarFeiler() throws Exception {
        assumeFalse(isWindowsOs());
        final File file = temp.newFolder(testName.getMethodName());
        assertThat(file.setReadable(false)).isTrue();
        exception.expect(ParameterException.class);
        exception.expectMessage("er ikke lesbar for batchen");
        validator.validate("bane", file.toPath());
        assertThat(file.setReadable(true)).isTrue();
    }

    @Test
    public void testStiIkkeSkrivbarFeiler() {
        assumeFalse(isWindowsOs());
        final File file = new File("/");
        exception.expect(ParameterException.class);
        exception.expectMessage("er ikke skrivbar for batchen");
        validator.validate("bane", file.toPath());
    }

    private boolean isWindowsOs(){
        return System.getProperty( "os.name" ).startsWith( "Windows" );
    }
}
