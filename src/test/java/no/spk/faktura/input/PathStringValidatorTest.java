package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class PathStringValidatorTest {

    private static final String SOME_PARAM = "-param";

    PathStringValidator validator;

    @Before
    public void setUp() {
        validator = new PathStringValidator();
    }

    @Test
    public void testIncorrectPathThrowsException() {
        assumeTrue( isWindows() );

        ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate(SOME_PARAM, "H\"", dummySpec()));
        assertTrue(exception.getMessage().contains(SOME_PARAM + " er ikke en gyldig filbane"));
    }

    @Test
    public void testCorrectPathValidatesWithoutException() {
        validator.validate(SOME_PARAM, "H:", dummySpec());
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
