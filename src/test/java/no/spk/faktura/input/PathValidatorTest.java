package no.spk.faktura.input;

import org.junit.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import java.nio.file.Paths;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PathValidatorTest {

    @Test
    public void skalIgnorereManglendeVerdi() {
        new PathValidator().validate("path", empty(), dummySpec());
    }

    @Test
    public void skalFeileVissFilenIkkeEksisterer() {
        ParameterException exception = assertThrows(ParameterException.class, () -> {
            new PathValidator().validate("path", of("yadayada.whatever").map(Paths::get), dummySpec());
        });
        assertTrue(exception.getMessage().contains("Filen yadayada.whatever eksisterer ikke"));
        assertTrue(exception.getMessage().contains("verifiser at du har angitt rett filnavn og -sti"));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
