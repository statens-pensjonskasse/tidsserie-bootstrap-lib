package no.spk.faktura.input;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class IntegerValidatorTest {

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalValidereIntegerOk() {
        new IntegerValidator().validate("i", "1", dummySpec());
    }

    @Test
    public void skalFeilePaaFloat() {
        e.expect(ParameterException.class);
        new IntegerValidator().validate("i", "1.2", dummySpec());
    }

    @Test
    public void skalFeilePaaTekst() {
        e.expect(ParameterException.class);
        new IntegerValidator().validate("i", "t", dummySpec());
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
