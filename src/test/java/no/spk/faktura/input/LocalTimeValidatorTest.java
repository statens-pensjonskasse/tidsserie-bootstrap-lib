package no.spk.faktura.input;

import org.junit.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import static org.junit.Assert.assertThrows;

public class LocalTimeValidatorTest {

    @Test
    public void skalValidereIntegerOk() {
        new LocalTimeValidator().validate("i", "0230", dummySpec());
    }

    @Test
    public void skalValidereTimeWithColonOk() {
        new LocalTimeValidator().validate("i", "02:30", dummySpec());
    }

    @Test
    public void skalFeilePaaFloat() {
        assertThrows(ParameterException.class, () -> new LocalTimeValidator().validate("i", "1.2", dummySpec()));
    }

    @Test
    public void skalFeilePaaTekst() {
        assertThrows(ParameterException.class, () -> new LocalTimeValidator().validate("i", "t", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
