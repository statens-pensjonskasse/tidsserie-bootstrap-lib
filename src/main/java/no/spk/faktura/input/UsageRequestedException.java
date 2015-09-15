package no.spk.faktura.input;

import com.beust.jcommander.JCommander;

/**
 * Exception som kastes dersom bruker har angitt ønske om hjelp.
 */
public class UsageRequestedException extends RuntimeException {
    private static final long serialVersionUID = -1;

    private final JCommander jCommander;

    UsageRequestedException(final JCommander jCommander) {
        this.jCommander = jCommander;
    }

    /**
     * Konstruerer en hjelpetekst for programmet vha {@link JCommander#usage()}
     * @return en streng som beskriver bruk av programmet.
     */
    public String usage() {
        final StringBuilder usage = new StringBuilder();
        jCommander.usage(usage);
        return usage.toString();
    }
}
