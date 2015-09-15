package no.spk.faktura.input;


import com.beust.jcommander.ParameterException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IntegerValidatorTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalValidereIntegerOk() {
        new IntegerValidator().validate("i", "1");
    }

    @Test
    public void skalFeilePaaFloat() {
        e.expect(ParameterException.class);
        new IntegerValidator().validate("i", "1.2");
    }

    @Test
    public void skalFeilePaaTekst() {
        e.expect(ParameterException.class);
        new IntegerValidator().validate("i", "t");
    }
}
