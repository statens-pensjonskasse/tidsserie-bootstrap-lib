package no.spk.faktura.input;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

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
    public void testRequiredArgIsRequired() {
        exception.expect(InvalidParameterException.class);
        exception.expectMessage("-r");
        factory.create();
    }

    @Test
    public void testUnknownOptionThrows() {
        exception.expect(InvalidParameterException.class);
        exception.expectMessage("-unknown");
        factory.create("-r", "test", "-unknown");
    }

    @Test
    public void testRequiredArgsIsSet() {
        assertThat(factory.create("-r", "test").required).isEqualTo("test");
    }

    @Test
    public void testHelpRequestedThrowsException() {
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
    public void testPostValidationCanBeSkipped() {
        final TestParameters test = new TestParameters();
        final CommandLine cmd = new CommandLine(test);
        factory = new ProgramArgumentsFactory<>(TestParameters.class,
                (a, b) -> {
                    throw new ParameterException(cmd, "skal ikke kastes");
                });
        factory.create(false, "-r", "test");
    }

    @Test
    public void testPostValidationCanThrow() {
        final TestParameters test = new TestParameters();
        final CommandLine cmd = new CommandLine(test);

        exception.expect(InvalidParameterException.class);
        final String expectedMessage = "skal kastes";
        exception.expectMessage(expectedMessage);
        factory = new ProgramArgumentsFactory<>(TestParameters.class,
                (a, b) -> {
                    throw new ParameterException(cmd, expectedMessage);
                });
        factory.create(true, "-r", "test");
    }

    @Test
    public void testPostValidationShouldNotRunWhenHelpIsRequested() {
        final TestParameters test = new TestParameters();
        final CommandLine cmd = new CommandLine(test);

        exception.expect(UsageRequestedException.class);
        factory = new ProgramArgumentsFactory<>(TestParameters.class,
                (a, b) -> {
                    throw new ParameterException(cmd, "skal ikke kastes");
                });
        factory.create(true, "-h");
    }
}
