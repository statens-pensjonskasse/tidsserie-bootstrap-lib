package no.spk.faktura.input;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class PathValidatorTest {

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void skalIgnorereManglendeVerdi() {
        new PathValidator().validate("path", empty());
    }

    @Test
    public void skalFeileVissFilenIkkeEksisterer() {
        e.expect(ParameterException.class);
        e.expectMessage("Filen yadayada.whatever eksisterer ikke");
        e.expectMessage("verifiser at du har angitt rett filnavn og -sti");
        new PathValidator().validate("path", of("yadayada.whatever").map(Paths::get));
    }
}
