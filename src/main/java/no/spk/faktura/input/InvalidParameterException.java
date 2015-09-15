package no.spk.faktura.input;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * Kastes dersom {@link ProgramArgumentsFactory#create(String...)} blir kalt med ugyldige verdier.
 * @author Snorre E. Brekke - Computas
 */
public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = -1;

    private final String usageString;

    InvalidParameterException(String usageString, final ParameterException cause) {
        super(cause.getMessage());
        this.usageString = usageString;
    }

    /**
     * Konstruerer en hjelpetekst for programmet vha {@link JCommander#usage()}
     * @return en streng som beskriver bruk av programmet.
     */
    public String usage() {
        return usageString;
    }
}
