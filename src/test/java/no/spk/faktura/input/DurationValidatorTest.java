package no.spk.faktura.input;

import org.junit.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import static org.junit.Assert.assertThrows;

public class DurationValidatorTest {

    @Test
    public void skalValidere4SifferOk() {
        new DurationValidator().validate("i", "9999", dummySpec());
    }

    @Test
    public void skalFeilePaaMindreEnnFireSiffer() {
        assertThrows(ParameterException.class, () ->  new DurationValidator().validate("i", "123", dummySpec()));
    }

    @Test
    public void skalFeilePaaMerEnnFireSiffer() {
        assertThrows(ParameterException.class, () -> new DurationValidator().validate("i", "12345", dummySpec()));
    }

    @Test
    public void skalFeilePaaTekst() {
        assertThrows(ParameterException.class, () -> new DurationValidator().validate("i", "123t", dummySpec()));
    }

    @Test
    public void skalFeilePaaNegativVerdi() {
        assertThrows(ParameterException.class, () -> new DurationValidator().validate("i", "-123", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
