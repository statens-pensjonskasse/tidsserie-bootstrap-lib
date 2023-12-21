package no.spk.faktura.input;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.Option;

/**
 * Tester oversetting av picocli-feilmeldinger.
 */
class InvalidParameterExceptionTest {

    private ProgramArgumentsFactory<?> factory;

    @BeforeEach
    void setUp() {
        factory = new ProgramArgumentsFactory<>(ArgumentsTest.class);
    }

    @Test
    void testOversettPaakrevd() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create());
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Følgende valg er påkrevd: -r"));
    }

    @Test
    void testGjentatteValg() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-r", "abc"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Kan bare angi -r én gang."));
    }

    @Test
    void testFeilEnum() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-e", "X"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Ugyldig verdi for -e. Lovlige verdier: [A, B]."));
    }

    @Test
    void testForeventetVerdi() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Forventet en verdi etter -r."));
    }

    @Test
    void testMainParameterOversatt() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "what"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Uventet parameter 'what'."));
    }

    @Test
    void testFlereVerdierForEnParameterMangler() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-m", "x"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Forventet 2 verdier etter -m."));
    }

    @Test
    void testFeilIntegerVerdi() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "abc", "-o", "x"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: \"-o\": Kunne ikke konvertere \"x\" til et heltall."));
    }

    @Test
    void testUkjentValg() {
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> factory.create("-r", "test", "-u"));
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Ukjent valg: -u"));
    }

    static class ArgumentsTest extends ArgumentsAdapter {
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
