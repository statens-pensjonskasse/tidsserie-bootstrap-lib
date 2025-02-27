package no.spk.tidsserie.input;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

class ProgramArgumentsFactoryTest {

    @TempDir
    Path temp;

    private ProgramArgumentsFactory<TestParameters> factory;

    @BeforeEach
    void setUp() {
        factory = new ProgramArgumentsFactory<>(TestParameters.class);
    }

    @Test
    void testRequiredArgIsRequired() {
        final InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> factory.create());
        assertTrue(exception.getMessage().contains("-r"));
    }

    @Test
    void testUnknownOptionThrows() {
        final InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "test", "-unknown"));
        assertTrue(exception.getMessage().contains("-unknown"));
    }

    @Test
    void testRequiredArgsIsSet() {
        assertThat(factory.create("-r", "test").required).isEqualTo("test");
    }

    @Test
    void testHelpRequestedThrowsException() {
        assertThrows(UsageRequestedException.class, () -> factory.create("-r", "test", "-h"));
    }

    @Test
    void testOptionalUrlSet() {
        final String expected = "jdbc:sqlserver://syb08t.spk.no:4100;databaseName=CI_TRUNK";
        final TestParameters arguments = factory.create(
                "-r", "yadayada",
                "-url", expected
        );
        assertThat(arguments.url).as("JDBC URL").isEqualTo(of(expected));
    }

    @Test
    void skalPopulereDatabasePassordfil() throws IOException {
        final Path expected = Files.createFile(temp.resolve("ny-fil"));
        final TestParameters arguments = factory.create(
                "-r", "yadayada",
                "-pw", expected.toString()
        );
        assertThat(arguments.password).as("JDBC passwordfile").isEqualTo(of(expected));
    }

    @Test
    void testPostValidationCanBeSkipped() {
        final TestParameters test = new TestParameters();
        final CommandLine cmd = new CommandLine(test);
        factory = new ProgramArgumentsFactory<>(TestParameters.class,
                (a, b) -> {
                    throw new ParameterException(cmd, "skal ikke kastes");
                });
        factory.create(false, "-r", "test");
    }

    @Test
    void testPostValidationCanThrow() {
        final TestParameters test = new TestParameters();
        final CommandLine cmd = new CommandLine(test);
        final String expectedMessage = "skal kastes";

        final InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            factory = new ProgramArgumentsFactory<>(TestParameters.class,
                    (a, b) -> {
                        throw new ParameterException(cmd, expectedMessage);
                    });
            factory.create(true, "-r", "test");
        });
        assertTrue(exception.getMessage().contains("skal kastes"));
    }

    @Test
    void testPostValidationShouldNotRunWhenHelpIsRequested() {
        final TestParameters test = new TestParameters();
        final CommandLine cmd = new CommandLine(test);

        assertThrows(UsageRequestedException.class, () -> {
            factory = new ProgramArgumentsFactory<>(TestParameters.class,
                    (a, b) -> {
                        throw new ParameterException(cmd, "skal ikke kastes");
                    });
            factory.create(true, "-h");
        });
    }
}
