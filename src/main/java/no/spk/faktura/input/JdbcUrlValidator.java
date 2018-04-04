package no.spk.faktura.input;

import java.util.regex.Pattern;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Validator som validerer JDBC URLen som brukaren angir på kommandolinja.
 * <p>
 * Valideringa verifiserer at URLen er på forma jdbc:jtds:(sybase|sqlserver)://&lt;HOSTNAME&gt;:&lt;PORT&gt;/&lt;DATABASE&gt;.
 *
 * @author Tarjei Skorgenes
 */
public class JdbcUrlValidator implements IParameterValidator {
    static final Pattern URL_PATTERN = Pattern.compile("^jdbc:jtds:(?:sybase|sqlserver)://([^:]+):([^/]+)/([^;]+)$");

    @Override
    public void validate(final String name, final String value) throws ParameterException {
        if (!URL_PATTERN.matcher(value).find()) {
            throw new ParameterException(
                    String.format(
                            "Parameter %s må inneholde en gyldig JDBC-url på formen '%s' eller '%s'",
                            name,
                            "jdbc:jtds:sybase://<server>:<port>/<database>",
                            "jdbc:jtds:sqlserver://<server>:<port>/<database>"
                    )
            );
        }
    }
}
