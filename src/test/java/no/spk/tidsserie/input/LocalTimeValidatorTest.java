package no.spk.tidsserie.input;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class LocalTimeValidatorTest {

    @Test
    void skalValidereIntegerOk() {
        new LocalTimeValidator().validate("i", "0230", dummySpec());
    }

    @Test
    void skalValidereTimeWithColonOk() {
        new LocalTimeValidator().validate("i", "02:30", dummySpec());
    }

    @Test
    void skalFeilePaaFloat() {
        assertThrows(ParameterException.class, () -> new LocalTimeValidator().validate("i", "1.2", dummySpec()));
    }

    @Test
    void skalFeilePaaTekst() {
        assertThrows(ParameterException.class, () -> new LocalTimeValidator().validate("i", "t", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
