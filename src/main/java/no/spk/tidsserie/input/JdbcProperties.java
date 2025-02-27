package no.spk.tidsserie.input;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;

import java.util.regex.Matcher;

/**
 * Holder på jdbcUrl angitt fra kommandolinje eller spk.properties, med ";encrypt=true;trustServerCertificate=true;ApplicationName=applikasjonsavn" på slutten,
 * samt brukernavn og passord for databasen hentet fra samme konfigurasjonskilde.
 * Støtter kun jdbcurl med formatet {@code jdbc:sqlserver://<server>:<port>;databaseName=<database>}.
 */
public class JdbcProperties {
    private final String url;
    private final String brukernavn;
    private final String passord;
    private final String server;
    private final String port;
    private final String database;

    public JdbcProperties(final String applikasjonsnavn, final String jdbcUrl, final String brukernavn, final String passord) {
        requireNonNull(applikasjonsnavn, "applikasjonsnavn kan ikke være null");
        requireNonNull(jdbcUrl, "jdbcUrl kan ikke være null");
        requireNonNull(brukernavn, "brukernavn kan ikke være null");
        requireNonNull(passord, "passord kan ikke være null");
        this.url = jdbcUrl + ";encrypt=true;trustServerCertificate=true;ApplicationName=" + applikasjonsnavn;
        this.brukernavn = brukernavn;
        this.passord = passord;

        final Matcher urlMatcher = getMatcher(jdbcUrl);
        this.server = urlMatcher.group(1);

        if (urlMatcher.group(3) != null) { // Angitt med portnummer.
            this.port = urlMatcher.group(3);
        } else { // Angitt uten portnummer.
            this.port = "";
        }

        this.database = urlMatcher.group(4);
    }

    /**
     * @return jdbcUrl som brukes for databasetilkobling
     */
    public String url() {
        return url;
    }

    /**
     * @return brukernavn som brukes for databasetilkobling
     */
    public String brukernavn() {
        return brukernavn;
    }

    /**
     * @return brukernavn som brukes for passord
     */
    public String passord() {
        return passord;
    }

    /**
     * @return server fra url
     */
    public String server() {
        return server;
    }

    /**
     * @return port fra url
     */
    public String port() {
        return port;
    }

    /**
     * @return databasenavn fra url
     */
    public String database() {
        return database;
    }

    @Override
    public String toString() {
        return "Url: " + url + " - brukernavn: " + brukernavn;
    }

    private Matcher getMatcher(final String jdbcUrl) {
        return of(jdbcUrl)
                .map(JdbcUrlValidator.URL_PATTERN::matcher)
                .filter(Matcher::find)
                .orElseThrow(() -> new IllegalArgumentException(jdbcUrl + " er ikke en lovlig jdbc-url. " +
                        "Url må ha formatet 'jdbc:sqlserver://<server>:<port>;database=<database>' eller " +
                        "'jdbc:sqlserver://<server>:<port>;databaseName=<database>', eventuelt uten port."));
    }
}
