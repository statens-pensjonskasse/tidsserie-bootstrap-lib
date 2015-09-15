package no.spk.faktura.input;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Validator som grovvaliderer JDBC URLen som brukaren angit p� kommandolinja.
 * <p>
 * Valideringa verifiserer kun at URLen startar med jdbc:something:something:something, ikkje at det er JTDS, sybase osv.
 *
 * @author Tarjei Skorgenes
 */
public class JdbcUrlValidator implements IParameterValidator {
    @Override
    public void validate(final String name, final String value) throws ParameterException {
        if (!value.matches("^jdbc:[^:]+:.+")) {
            throw new ParameterException(
                    "Parameter "
                            + name
                            + " m� inneholde en gyldig JDBC-url p� formen 'jdbc:<subprotocol>:<subname>'"
            );
        }
    }
}
