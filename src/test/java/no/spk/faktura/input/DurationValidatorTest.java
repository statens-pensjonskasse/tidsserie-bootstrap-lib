package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class DurationValidatorTest {

    private TestParameters parameters;

    @Before
    public void setup() {
        parameters = new TestParameters();
        new CommandLine(parameters).parseArgs("-r", "test", "-d", "0200", "-url", "jdbc:jtds:sqlserver://jalla:1234/testdb", "-t", "0200", "-pw", "README.md");
    }

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalValidere4SifferOk() {
        new DurationValidator().validate("i", "9999", parameters.spec.commandLine());
    }

    @Test
    public void skalFeilePaaMindreEnnFireSiffer() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "123", parameters.spec.commandLine());
    }

    @Test
    public void skalFeilePaaMerEnnFireSiffer() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "12345", parameters.spec.commandLine());
    }

    @Test
    public void skalFeilePaaTekst() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "123t", parameters.spec.commandLine());
    }

    @Test
    public void skalFeilePaaNegativVerdi() {
        e.expect(ParameterException.class);
        new DurationValidator().validate("i", "-123", parameters.spec.commandLine());
    }
}
