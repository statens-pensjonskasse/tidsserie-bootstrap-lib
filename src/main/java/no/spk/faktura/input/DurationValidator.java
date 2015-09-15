package no.spk.faktura.input;


import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author Snorre E. Brekke - Computas
 */
public class DurationValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        DurationUtil.convert(value)
                .orElseThrow(() -> new ParameterException("'" + name + "': m� best� av 4 siffer p� formatet HHmm (fant " + value + ")."));
    }
}
