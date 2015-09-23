package no.spk.faktura.input;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * Kastes dersom {@link ProgramArgumentsFactory#create(String...)} blir kalt med ugyldige verdier.
 * @author Snorre E. Brekke - Computas
 */
public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = -1;

    private final String usageString;

    private static final List<Function<String, Optional<String>>> oversettere = Arrays.asList(
            (s) -> oversett("^The following options? (?:is|are) required: (.+)$", "Følgende valg er påkrevd: %s", s),
            (s) -> oversett("^Found the option (.+) multiple times$", "Fant valget %s flere ganger.", s),
            (s) -> oversett("^Can only specify option (.+) once.$", "Kan bare angi %s én gang.", s),
            (s) -> oversett("^Expected a value after parameter (.+)$", "Forventet en verdi etter %s.", s),
            (s) -> oversett("^Expected (.+) values after (.+)$", "Forventet %s verdier etter %s.", s),
            (s) -> oversett("^Invalid value for (.+) parameter. Allowed values:(.+)$", "Ugyldig verdi for %s. Lovlige verdier: %s.", s),
            (s) -> oversett("^(.+): couldn't convert (.+) to an integer$", "%s: Kunne ikke konvertere %s til et heltall.", s),
            (s) -> oversett("^Unknown option: (.+)$", "Ukjent valg: %s.", s)
    );

    InvalidParameterException(String usageString, final ParameterException cause) {
        super(hentMelding(cause));
        this.usageString = usageString;
    }

    /**
     * Konstruerer en hjelpetekst for programmet vha {@link JCommander#usage()}
     * @return en streng som beskriver bruk av programmet.
     */
    public String usage() {
        return usageString;
    }

    private static String hentMelding(ParameterException cause){
        final String opprinneligMelding = cause.getMessage();

        return oversettere.stream()
                .map((o) -> o.apply(opprinneligMelding))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseGet(() -> opprinneligMelding);
    }

    private static Optional<String> oversett(String regex, String oversattMelding, String melding) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(melding);
        if (matcher.matches()) {
            final int grupper = matcher.groupCount();
            String[] gruppeMatch = new String[grupper];
            for (int i = 0; i < grupper ; i++) {
                gruppeMatch[i] = matcher.group(i + 1);
            }
            return Optional.of(String.format(oversattMelding, gruppeMatch));
        }
        return Optional.empty();
    }
}
