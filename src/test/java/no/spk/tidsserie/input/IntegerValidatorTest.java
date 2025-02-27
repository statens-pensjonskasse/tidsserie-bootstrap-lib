package no.spk.tidsserie.input;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class IntegerValidatorTest {

    @Test
    void skalValidereIntegerOk() {
        new IntegerValidator().validate("i", "1", dummySpec());
    }

    @Test
    void skalFeilePaaFloat() {
        assertThrows(ParameterException.class, () -> new IntegerValidator().validate("i", "1.2", dummySpec()));
    }

    @Test
    void skalFeilePaaTekst() {
        assertThrows(ParameterException.class, () -> new IntegerValidator().validate("i", "t", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
