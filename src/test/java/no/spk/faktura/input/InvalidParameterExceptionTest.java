package no.spk.faktura.input;


import com.beust.jcommander.Parameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tester oversetting av JCommander feilmeldinger.
 * @author Snorre E. Brekke - Computas
 */
public class InvalidParameterExceptionTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    ProgramArgumentsFactory<?> factory;

    @Before
    public void setUp() throws Exception {
        factory = new ProgramArgumentsFactory<>(ArgumentsTest.class);
    }

    @Test
    public void testOversettPaakrevd() throws Exception {
        exception.expectMessage("Feil i parameter: Følgende valg er påkrevd: -r");
        factory.create();
    }

    @Test
    public void testGjentatteValg() throws Exception {
        exception.expectMessage("Kan bare angi -r én gang.");
        factory.create("-r", "abc", "-r", "abc");
    }

    @Test
    public void testFeilEnum() throws Exception {
        exception.expectMessage("Ugyldig verdi for -e. Lovlige verdier: [A, B].");
        factory.create("-r", "abc", "-e", "X");
    }

    @Test
    public void testForeventetVerdi() throws Exception {
        exception.expectMessage("Forventet en verdi etter -r.");
        factory.create("-r");
    }


    @Test
    public void testMainParameterOversatt() throws Exception {
        exception.expectMessage("Uventet parameter 'what'.");
        factory.create("-r", "abc", "what");
    }

    @Test
    public void testFlereVerdierForEnParameterMangler() throws Exception {
        exception.expectMessage("Forventet 2 verdier etter -m.");
        factory.create("-r", "abc", "-m", "x");
    }

    @Test
    public void testFeilIntegerVerdi() throws Exception {
        exception.expectMessage("\"-o\": Kunne ikke konvertere \"x\" til et heltall.");
        factory.create("-r", "abc", "-o", "x");
    }

    @Test
    public void testUkjentValg() throws Exception {
        exception.expectMessage("Ukjent valg: -u");
        factory.create("-u");
    }

    public static class ArgumentsTest extends ArgumentsAdapter {
        @Parameter(names = "-r", required = true)
        String req;

        @Parameter(names = "-o")
        int opt;

        @Parameter(names = "-m", arity = 2)
        int[] multiple;

        @Parameter(names = "-e")
        TestEnum enumverdi;
    }

    private static class ArgumentsAdapter implements Arguments{
        @Override
        public boolean hjelp() {
            return false;
        }
    }

    private enum TestEnum{
        A, B
    }
}