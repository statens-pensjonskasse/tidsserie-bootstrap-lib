package no.spk.faktura.input;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine.ParameterException;

public class LocalTimeValidatorTest {

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalValidereIntegerOk() {
        new LocalTimeValidator().validate("i", "0230");
    }

    @Test
    public void skalValidereTimeWithColonOk() {
        new LocalTimeValidator().validate("i", "02:30");
    }

    @Test
    public void skalFeilePaaFloat() {
        e.expect(ParameterException.class);
        new LocalTimeValidator().validate("i", "1.2");
    }

    @Test
    public void skalFeilePaaTekst() {
        e.expect(ParameterException.class);
        new LocalTimeValidator().validate("i", "t");
    }
}
