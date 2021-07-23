package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine.Option;

/**
 * Tester oversetting av picocli-feilmeldinger.
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
    public void testOversettPaakrevd() {
        exception.expectMessage("Feil i parameter: Følgende valg er påkrevd: -r");
        factory.create();
    }

    @Test
    public void testGjentatteValg() {
        exception.expectMessage("Feil i parameter: Kan bare angi -r én gang.");
        factory.create("-r", "abc", "-r", "abc");
    }

    @Test
    public void testFeilEnum() {
        exception.expectMessage("Feil i parameter: Ugyldig verdi for -e. Lovlige verdier: [A, B].");
        factory.create("-r", "abc", "-e", "X");
    }

    @Test
    public void testForeventetVerdi() {
        exception.expectMessage("Feil i parameter: Forventet en verdi etter -r.");
        factory.create("-r");
    }

    @Test
    public void testMainParameterOversatt() {
        exception.expectMessage("Feil i parameter: Uventet parameter 'what'.");
        factory.create("-r", "abc", "what");
    }

    @Test
    public void testFlereVerdierForEnParameterMangler() {
        exception.expectMessage("Feil i parameter: Forventet 2 verdier etter -m.");
        factory.create("-r", "abc", "-m", "x");
    }

    @Test
    public void testFeilIntegerVerdi() {
        exception.expectMessage("Feil i parameter: \"-o\": Kunne ikke konvertere \"x\" til et heltall.");
        factory.create("-r", "abc", "-o", "x");
    }

    @Test
    public void testUkjentValg() {
        exception.expectMessage("Feil i parameter: Ukjent valg: -u");
        factory.create("-r", "test", "-u");
    }

    public static class ArgumentsTest extends ArgumentsAdapter {
        @Option(names = "-r", required = true)
        String req;

        @Option(names = "-o")
        int opt;

        @Option(names = "-m", arity = "2")
        int[] multiple;

        @Option(names = "-e")
        TestEnum enumverdi;
    }

    private static class ArgumentsAdapter implements Arguments {
        @Override
        public boolean hjelp() {
            return false;
        }
    }

    private enum TestEnum {
        A, B
    }
}
