package no.spk.faktura.input;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import picocli.CommandLine.ParameterException;

/**
 * Kastes dersom {@link ProgramArgumentsFactory#create(String...)} blir kalt med ugyldige verdier.
 */
public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = -1;

    private final String usageString;

    private static final List<Function<String, Optional<String>>> oversettere = Arrays.asList(
            (s) -> oversett("^Missing required options and parameters: (.+)$", "Følgende valg er påkrevd: %s", s),
            (s) -> oversett("^Missing required option: '(.+)=(.*)'$", "Følgende valg er påkrevd: %s", s),
            (s) -> oversett("^Found the option (.+) multiple times$", "Fant valget %s flere ganger.", s),
            (s) -> oversett("^option '(.+)' \\(.*\\) should be specified only once$", "Kan bare angi %s én gang.", s),
            (s) -> oversett("^Missing required parameter for option '(.+)' \\(.*\\)$", "Forventet en verdi etter %s.", s),
            (s) -> oversett("^option '(.+)' at index \\d+ \\(<multiple>\\) requires at least (.+) values, but only (.+) were specified: (.*)$", "Forventet %2$s verdier etter %1$s.", s),
            (s) -> oversett("^Invalid value for option (.+): (.+) is not a (.+)$", "Ugyldig verdi for %s. %s er ikke en boolean", s),
            (s) -> oversett("^Invalid value for option '(.+)': expected one of \\[(.*)\\] \\(case-sensitive\\) but was '(.*)'$", "Ugyldig verdi for %s. Lovlige verdier: [%s].", s),
            (s) -> oversett("^Unmatched argument at index \\d+: (.*)$", "Uventet parameter %s.", s),
            (s) -> oversett("^Invalid value for option '(.+)': '(.+)' is not an int$", "\"%s\": Kunne ikke konvertere \"%s\" til et heltall.", s),
            (s) -> oversett("^Unknown option: '(.+)'$", "Ukjent valg: %s.", s)
    );

    public InvalidParameterException(final String usageString, final ParameterException cause) {
        super(hentMelding(cause));
        this.usageString = usageString;
    }

    /**
     * Konstruerer en hjelpetekst for programmet.
     *
     * @return en streng som beskriver bruk av programmet.
     */
    public String usage() {
        return usageString;
    }

    private static String hentMelding(ParameterException cause) {
        final String opprinneligMelding = cause.getMessage();

        return "Feil i parameter: " +
                oversettere.stream()
                        .map((o) -> o.apply(opprinneligMelding))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findAny()
                        .orElseGet(() -> opprinneligMelding);
    }

    private static Optional<String> oversett(final String regex, final String oversattMelding, final String melding) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(melding);
        if (matcher.matches()) {
            final int grupper = matcher.groupCount();
            final String[] gruppeMatch = new String[grupper];
            for (int i = 0; i < grupper; i++) {
                gruppeMatch[i] = matcher.group(i + 1);
            }
            return Optional.of(String.format(oversattMelding, (Object[]) gruppeMatch));
        }
        return Optional.empty();
    }
}
