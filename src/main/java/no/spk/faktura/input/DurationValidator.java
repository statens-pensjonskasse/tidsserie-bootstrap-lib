package no.spk.faktura.input;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author Snorre E. Brekke - Computas
 */
public class DurationValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (value.length() != 4) {
            throwParameterException(name, value);
        }
        try {
            int i = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throwParameterException(name, value);
        }
    }

    private void throwParameterException(String name, String value) {
        throw new ParameterException("'" + name + "': m� best� av 4 siffer p� formatet HHmm (fant " + value + ").");
    }
}
