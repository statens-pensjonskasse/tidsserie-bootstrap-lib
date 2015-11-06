package no.spk.faktura.input;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import com.beust.jcommander.ParameterException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * @author Snorre E. Brekke - Computas
 */
public class ProgramArgumentsFactoryTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    @Rule
    public final StandardOutputAndError console = new StandardOutputAndError();

    private String outPath;
    private ProgramArgumentsFactory<TestParameters> factory;

    @Before
    public void setUp() throws Exception {
        outPath = temp.newFolder().getAbsolutePath();
        factory = new ProgramArgumentsFactory<>(TestParameters.class);
    }

    @After
    public void _after() {
        console.assertStandardOutput().isEmpty();
        console.assertStandardError().isEmpty();
    }

    @Test
    public void testRequiredArgIsRequired() throws Exception {
        exception.expect(InvalidParameterException.class);
        exception.expectMessage("-r");
        factory.create();
    }

    @Test
    public void testUnknownOptionThrows() throws Exception {
        exception.expect(InvalidParameterException.class);
        exception.expectMessage("-unknown");
        factory.create("-unknown");
    }

    @Test
    public void testRequiredArgsIsSet() throws Exception {
        assertThat(factory.create("-r", "test").required).isEqualTo("test");
    }

    @Test
    public void testHelpRequestedThrowsException() throws Exception {
        exception.expect(UsageRequestedException.class);
        factory.create("-r", "test", "-h");
    }

    @Test
    public void testOptionalUrlSet() {
        final String expected = "jdbc:jtds:sybase://syb08t.spk.no:4100/CI_TRUNK";
        final TestParameters arguments = factory.create(
                "-r", "yadayada",
                "-url", expected
        );
        assertThat(arguments.url).as("JDBC URL").isEqualTo(of(expected));
    }

    @Test
    public void skalPopulereDatabasePassordfil() throws IOException {
        final Path expected = temp.newFile().toPath();
        final TestParameters arguments = factory.create(
                "-r", "yadayada",
                "-pw", expected.toString()
        );
        assertThat(arguments.password).as("JDBC passwordfile").isEqualTo(of(expected));
    }

    @Test
    public void testPostValidationCanBeSkipped() throws Exception {
        factory = new ProgramArgumentsFactory<>(TestParameters.class,
                (a) -> {throw new ParameterException("skal ikke kastes");});
        factory.create(false, "-r", "test");
    }

    @Test
    public void testPostValidationCanThrow() throws Exception {
        exception.expect(InvalidParameterException.class);
        final String expectedMessage = "skal kastes";
        exception.expectMessage(expectedMessage);
        factory = new ProgramArgumentsFactory<>(TestParameters.class,
                (a) -> { throw new ParameterException(expectedMessage);});
        factory.create(true, "-r", "test");
    }

    @Test
    public void testPostValidationShouldNotRunWhenHelpIsRequested() throws Exception {
        exception.expect(UsageRequestedException.class);
        factory = new ProgramArgumentsFactory<>(TestParameters.class,
                (a) -> { throw new ParameterException("skal ikke kastes");});
        factory.create(true, "-h");
    }
}
