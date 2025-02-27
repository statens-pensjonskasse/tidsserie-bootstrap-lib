package no.spk.tidsserie.input;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class DurationValidatorTest {

    @Test
    void skalValidere4SifferOk() {
        new DurationValidator().validate("i", "9999", dummySpec());
    }

    @Test
    void skalFeilePaaMindreEnnFireSiffer() {
        assertThrows(ParameterException.class, () -> new DurationValidator().validate("i", "123", dummySpec()));
    }

    @Test
    void skalFeilePaaMerEnnFireSiffer() {
        assertThrows(ParameterException.class, () -> new DurationValidator().validate("i", "12345", dummySpec()));
    }

    @Test
    void skalFeilePaaTekst() {
        assertThrows(ParameterException.class, () -> new DurationValidator().validate("i", "123t", dummySpec()));
    }

    @Test
    void skalFeilePaaNegativVerdi() {
        assertThrows(ParameterException.class, () -> new DurationValidator().validate("i", "-123", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
