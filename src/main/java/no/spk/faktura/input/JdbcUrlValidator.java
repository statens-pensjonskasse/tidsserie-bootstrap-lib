package no.spk.faktura.input;

import java.util.regex.Pattern;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

/**
 * Validator som validerer JDBC URLen som brukaren angir på kommandolinja.
 * <p>
 * Valideringa verifiserer at URLen er på forma jdbc:jtds:(sybase|sqlserver)://&lt;HOSTNAME&gt;:&lt;PORT&gt;/&lt;DATABASE&gt;.
 */
public class JdbcUrlValidator {
    static final Pattern URL_PATTERN = Pattern.compile("^jdbc:jtds:(?:sybase|sqlserver)://([^:]+):([^/]+)/([^;]+)$");

    public void validate(final String name, final String value, final CommandSpec spec) throws ParameterException {
        if (!URL_PATTERN.matcher(value).find()) {
            throw new ParameterException(
                    new CommandLine(spec),
                    String.format(
                            "Parameter %s må inneholde en gyldig JDBC-url på formen '%s' eller '%s', du sendte inn %s",
                            name,
                            "jdbc:jtds:sybase://<server>:<port>/<database>",
                            "jdbc:jtds:sqlserver://<server>:<port>/<database>",
                            value
                    )
            );
        }
    }
}
