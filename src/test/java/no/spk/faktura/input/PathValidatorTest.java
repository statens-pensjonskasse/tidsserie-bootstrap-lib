package no.spk.faktura.input;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

class PathValidatorTest {

    @Test
    void skalIgnorereManglendeVerdi() {
        new PathValidator().validate("path", empty(), dummySpec());
    }

    @Test
    void skalFeileVissFilenIkkeEksisterer() {
        final ParameterException exception = assertThrows(
                ParameterException.class,
                () -> new PathValidator().validate("path", of("yadayada.whatever").map(Paths::get), dummySpec())
        );
        assertTrue(exception.getMessage().contains("Filen yadayada.whatever eksisterer ikke"));
        assertTrue(exception.getMessage().contains("verifiser at du har angitt rett filnavn og -sti"));
    }

    private CommandSpec dummySpec() {
        return CommandSpec.create();
    }
}
