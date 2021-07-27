package no.spk.faktura.input;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class DurationValidatorTest {

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalValidere4SifferOk() {
        new DurationValidator().validate("i", "9999", dummySpec());
    }

    @Test
    public void skalFeilePaaMindreEnnFireSiffer() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "123", dummySpec());
    }

    @Test
    public void skalFeilePaaMerEnnFireSiffer() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "12345", dummySpec());
    }

    @Test
    public void skalFeilePaaTekst() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "123t", dummySpec());
    }

    @Test
    public void skalFeilePaaNegativVerdi() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "-123", dummySpec());
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
