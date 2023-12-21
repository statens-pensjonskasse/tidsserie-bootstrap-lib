package no.spk.faktura.input;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class PathStringValidatorTest {

    private static final String SOME_PARAM = "-param";

    private final PathStringValidator validator = new PathStringValidator();

    @Test
    void testIncorrectPathThrowsException() {
        assumeTrue(isWindows());

        final ParameterException exception = assertThrows(ParameterException.class, () -> validator.validate(SOME_PARAM, "H\"", dummySpec()));
        assertTrue(exception.getMessage().contains(SOME_PARAM + " er ikke en gyldig filbane"));
    }

    @Test
    void testCorrectPathValidatesWithoutException() {
        validator.validate(SOME_PARAM, "H:", dummySpec());
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
