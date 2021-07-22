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
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class WritableDirectoryValidatorTest {

    private TestParameters parameters;

    @Before
    public void setup() {
        parameters = new TestParameters();
        new CommandLine(parameters).parseArgs("-r", "test", "-d", "0200", "-url", "jdbc:jtds:sqlserver://jalla:1234/testdb", "-t", "0200", "-pw", "README.md");
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    @Rule
    public final TestName testName = new TestName();

    WritableDirectoryValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new WritableDirectoryValidator();
    }

    @Test
    public void testManglendeStiFeiler() throws Exception {
        exception.expect(ParameterException.class);
        exception.expectMessage("eksisterer ikke");
        validator.validate("bane", Paths.get("nowhere"), parameters.spec.commandLine());
    }

    @Test
    public void testStiErFilFeiler() throws Exception {
        final File file = temp.newFile(testName.getMethodName());
        exception.expect(ParameterException.class);
        exception.expectMessage("peker ikke til en katalog");
        validator.validate("bane", file.toPath(), parameters.spec.commandLine());
    }

    @Test
    public void testStiIkkeLesbarFeiler() throws Exception {
        assumeFalse(isWindowsOs());
        final File file = temp.newFolder(testName.getMethodName());
        assertThat(file.setReadable(false)).isTrue();
        exception.expect(ParameterException.class);
        exception.expectMessage("er ikke lesbar for batchen");
        validator.validate("bane", file.toPath(), parameters.spec.commandLine());
        assertThat(file.setReadable(true)).isTrue();
    }

    @Test
    public void testStiIkkeSkrivbarFeiler() throws Exception {
        assumeFalse(isWindowsOs());
        final File file = new File("/");
        exception.expect(ParameterException.class);
        exception.expectMessage("er ikke skrivbar for batchen");
        validator.validate("bane", file.toPath(), parameters.spec.commandLine());
    }

    private boolean isWindowsOs(){
        return System.getProperty( "os.name" ).startsWith( "Windows" );
    }
}
