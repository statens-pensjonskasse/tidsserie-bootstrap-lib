package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class LocalTimeValidatorTest {

    private TestParameters parameters;

    @Before
    public void setup() {
        parameters = new TestParameters();
        new CommandLine(parameters).parseArgs("-r", "test", "-d", "0200", "-url", "jdbc:jtds:sqlserver://jalla:1234/testdb", "-t", "0200", "-pw", "README.md");
    }

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalValidereIntegerOk() {
        new LocalTimeValidator().validate("i", "0230", parameters.spec.commandLine());
    }

    @Test
    public void skalValidereTimeWithColonOk() {
        new LocalTimeValidator().validate("i", "02:30", parameters.spec.commandLine());
    }

    @Test
    public void skalFeilePaaFloat() {
        e.expect(ParameterException.class);
        new LocalTimeValidator().validate("i", "1.2", parameters.spec.commandLine());
    }

    @Test
    public void skalFeilePaaTekst() {
        e.expect(ParameterException.class);
        new LocalTimeValidator().validate("i", "t", parameters.spec.commandLine());
    }
}
