package no.spk.faktura.input;

import static org.junit.Assume.assumeTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class PathStringValidatorTest {

    private TestParameters parameters;

    @Before
    public void setup() {
        parameters = new TestParameters();
        new CommandLine(parameters).parseArgs("-r", "test", "-d", "0200", "-url", "jdbc:jtds:sqlserver://jalla:1234/testdb", "-t", "0200", "-pw", "README.md");
    }

    private static final String SOME_PARAM = "-param";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    PathStringValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new PathStringValidator();
    }

    @Test
    public void testIncorrectPathThrowsException() throws Exception {
        assumeTrue( isWindows() );
        exception.expect(ParameterException.class);
        exception.expectMessage(SOME_PARAM + " er ikke en gyldig filbane");
        validator.validate(SOME_PARAM, "H\"", parameters.spec.commandLine());
    }

    @Test
    public void testCorrectPathValidatesWithoutException() throws Exception {
        validator.validate(SOME_PARAM, "H:", parameters.spec.commandLine());
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
