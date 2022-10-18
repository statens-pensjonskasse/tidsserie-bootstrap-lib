package no.spk.faktura.input;

import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine.Option;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tester oversetting av picocli-feilmeldinger.
 */
public class InvalidParameterExceptionTest {

    ProgramArgumentsFactory<?> factory;

    @Before
    public void setUp() {
        factory = new ProgramArgumentsFactory<>(ArgumentsTest.class);
    }

    @Test
    public void testOversettPaakrevd() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create());
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Følgende valg er påkrevd: -r"));
    }

    @Test
    public void testGjentatteValg() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-r", "abc"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Kan bare angi -r én gang."));
    }

    @Test
    public void testFeilEnum() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-e", "X"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Ugyldig verdi for -e. Lovlige verdier: [A, B]."));
    }

    @Test
    public void testForeventetVerdi() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Forventet en verdi etter -r."));
    }

    @Test
    public void testMainParameterOversatt() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "what"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Uventet parameter 'what'."));
    }

    @Test
    public void testFlereVerdierForEnParameterMangler() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-m", "x"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Forventet 2 verdier etter -m."));
    }

    @Test
    public void testFeilIntegerVerdi() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-o", "x"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: \"-o\": Kunne ikke konvertere \"x\" til et heltall."));
    }

    @Test
    public void testUkjentValg() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "test", "-u"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Ukjent valg: -u"));
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
