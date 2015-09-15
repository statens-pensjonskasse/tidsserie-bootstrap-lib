package no.spk.faktura.input;


import com.beust.jcommander.ParameterException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DurationValidatorTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalValidere4SifferOk() {
        new DurationValidator().validate("i", "9999");
    }

    @Test
    public void skalFeilePaaMindreEnnFireSiffer() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "123");
    }

    @Test
    public void skalFeilePaaMerEnnFireSiffer() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "12345");
    }

    @Test
    public void skalFeilePaaTekst() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "123t");
    }

    @Test
    public void skalFeilePaaNegativVerdi() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "-123");
    }
}
