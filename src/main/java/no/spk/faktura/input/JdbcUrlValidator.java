package no.spk.faktura.input;

import java.util.regex.Pattern;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Validator som grovvaliderer JDBC URLen som brukaren angit på kommandolinja.
 * <p>
 * Valideringa verifiserer kun at URLen startar med jdbc:something:something:something, ikkje at det er JTDS, sybase osv.
 *
 * @author Tarjei Skorgenes
 */
public class JdbcUrlValidator implements IParameterValidator {
    static final Pattern URL_PATTERN = Pattern.compile("^jdbc:jtds:sybase://([^:]+):([^/]+)/([^;]+)$");

    @Override
    public void validate(final String name, final String value) throws ParameterException {
        if (!URL_PATTERN.matcher(value).find()) {
            throw new ParameterException(
                    "Parameter " +
                            name +
                            " må inneholde en gyldig JDBC-url på formen " +
                            "'jdbc:jtds:sybase://<server>:<port>/<database>'"
            );
        }
    }
}
