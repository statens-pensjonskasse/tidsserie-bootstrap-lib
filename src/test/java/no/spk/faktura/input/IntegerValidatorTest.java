package no.spk.faktura.input;

import org.junit.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import static org.junit.Assert.assertThrows;

public class IntegerValidatorTest {

    @Test
    public void skalValidereIntegerOk() {
        new IntegerValidator().validate("i", "1", dummySpec());
    }

    @Test
    public void skalFeilePaaFloat() {
        assertThrows(ParameterException.class, () -> new IntegerValidator().validate("i", "1.2", dummySpec()));
    }

    @Test
    public void skalFeilePaaTekst() {
        assertThrows(ParameterException.class, () -> new IntegerValidator().validate("i", "t", dummySpec()));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
