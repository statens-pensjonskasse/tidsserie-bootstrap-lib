package no.spk.tidsserie.input;

import java.util.regex.Pattern;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

/**
 * Validator som validerer JDBC URLen som brukaren angir på kommandolinja.
 * <br><br>
 * Valideringa verifiserer at URLen er på forma jdbc:sqlserver://<HOSTNAME>:<PORT>;(database|databaseName)=<DATABASE>,
 * eventuelt uten portnummer angitt: jdbc:sqlserver://<HOSTNAME>;(database|databaseName)=<DATABASE>.
 * <br><br>
 * Se <a href="https://learn.microsoft.com/en-us/sql/connect/jdbc/building-the-connection-url?view=sql-server-ver16&redirectedfrom=MSDN">MSDN-dokumentasjonen</a>
 * for mer info om hvordan formatet er bygget opp. Har per dags dato kun støtte for egenskapen databaseName.
 */
public class JdbcUrlValidator {
    static final Pattern URL_PATTERN = Pattern.compile("^jdbc:sqlserver://([^:]+)(:([^;]+))?;(?:database|databaseName)=([^;]+)$");

    public void validate(final String name, final String value, final CommandSpec spec) throws ParameterException {
        if (!URL_PATTERN.matcher(value).find()) {
            throw new ParameterException(
                    new CommandLine(spec),
                    String.format(
                            "Parameter %s må inneholde en gyldig JDBC-url på formen '%s' eller '%s', eventuelt uten portnummer, du sendte inn %s",
                            name,
                            "jdbc:sqlserver://<server>:<port>;database=<database>",
                            "jdbc:sqlserver://<server>:<port>;databaseName=<database>",
                            value
                    )
            );
        }
    }
}
