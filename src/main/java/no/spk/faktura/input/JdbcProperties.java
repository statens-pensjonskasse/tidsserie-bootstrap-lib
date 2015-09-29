package no.spk.faktura.input;

import static java.util.Objects.requireNonNull;

/**
 * Holder på jdbcUrl angitt fra kommandolinje eller spk.properties, med ";appName=applikasjonsavn" på slutten,
 * samt brukernavn og passord for databasen hentet fra samme konfigurasjonskilde.
 * @author Snorre E. Brekke - Computas
 */
public class JdbcProperties {
    private final String url;
    private final String brukernavn;
    private final String passord;

    public JdbcProperties(String applikasjonsnavn, String jdbcUrl, String brukernavn, String passord) {
        requireNonNull(applikasjonsnavn, "applikasjonsnavn kan ikke være null");
        requireNonNull(jdbcUrl, "jdbcUrl kan ikke være null");
        requireNonNull(brukernavn, "brukernavn kan ikke være null");
        requireNonNull(passord, "passord kan ikke være null");
        this.url = jdbcUrl + ";appName=" + applikasjonsnavn;
        this.brukernavn = brukernavn;
        this.passord = passord;
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

    @Override
    public String toString() {
        return "Url: " + url + " - brukernavn: " + brukernavn;
    }
}
