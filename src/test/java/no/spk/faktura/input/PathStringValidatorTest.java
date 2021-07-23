package no.spk.faktura.input;

import static org.junit.Assume.assumeTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class PathStringValidatorTest {

    private static final String SOME_PARAM = "-param";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    PathStringValidator validator;

    @Before
    public void setUp() {
        validator = new PathStringValidator();
    }

    @Test
    public void testIncorrectPathThrowsException() {
        assumeTrue( isWindows() );
        exception.expect(ParameterException.class);
        exception.expectMessage(SOME_PARAM + " er ikke en gyldig filbane");
        validator.validate(SOME_PARAM, "H\"");
    }

    @Test
    public void testCorrectPathValidatesWithoutException() {
        validator.validate(SOME_PARAM, "H:");
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
