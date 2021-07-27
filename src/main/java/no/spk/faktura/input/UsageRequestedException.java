package no.spk.faktura.input;

/**
 * Exception som kastes dersom bruker har angitt ønske om hjelp.
 */
public class UsageRequestedException extends RuntimeException {
    private static final long serialVersionUID = -1;

    private final String usageString;

    UsageRequestedException(String usageString) {
        this.usageString = usageString;
    }

    /**
     * Konstruerer en hjelpetekst for programmet
     * @return en streng som beskriver bruk av programmet.
     */
    public String usage() {
        return usageString;
    }
}
