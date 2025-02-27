package no.spk.tidsserie.input;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine.Mixin;

class JdbcParametersDelegateTest {

    @TempDir
    Path temp;

    @Test
    void testFactoryForJdbcDelegateForTomArgs() {
        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
        final TestArgument testArgument = factory.create();
        assertThat(testArgument.jdbcParams.jdbcUrl.isPresent()).isFalse();
        assertThat(testArgument.jdbcParams.jdbcBrukernavn.isPresent()).isFalse();
        assertThat(testArgument.jdbcParams.jdbcPassordfil.isPresent()).isFalse();
    }

    @Test
    void testFactoryForJdbcDelegateParsesArgs() throws IOException {
        final Path expected = Files.createFile(temp.resolve("ny-fil"));

        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
        final TestArgument testArgument = factory.create(
                "-jdbcUrl", "jdbc:sqlserver://server:port;databaseName=database",
                "-jdbcBrukernavn", "user",
                "-jdbcPassordfil", expected.toString());
        assertThat(testArgument.jdbcParams.getJdbcUrl()).isEqualTo(of("jdbc:sqlserver://server:port;databaseName=database"));
        assertThat(testArgument.jdbcParams.getJdbcBrukernavn()).isEqualTo(of("user"));
        assertThat(testArgument.jdbcParams.getJdbcPassordfil()).isEqualTo(of(expected));
    }

    @Test
    void testFactoryForJdbcDelegateSkalValidereUrl() throws IOException {
        final Path expected = Files.createFile(temp.resolve("ny-fil"));
        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);

        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> factory.create(
                        "-jdbcUrl", "feilurl",
                        "-jdbcBrukernavn", "user",
                        "-jdbcPassordfil", expected.toString())
        );
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Parameter -jdbcUrl må inneholde en gyldig JDBC-url på formen " +
                "'jdbc:sqlserver://<server>:<port>;database=<database>' eller 'jdbc:sqlserver://<server>:<port>;databaseName=<database>', eventuelt uten portnummer, du sendte inn feilurl"));
    }

    @Test
    void testFactoryForJdbcDelegateSkalValiderePassordfilsti() {
        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);

        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> factory.create(
                        "-jdbcUrl", "jdbc:sqlserver://server:port;databaseName=database",
                        "-jdbcBrukernavn", "user",
                        "-jdbcPassordfil", "missing")
        );
        assertTrue(invalidParameterException.getMessage().contains("missing eksisterer ikke"));
    }

    static class TestArgument implements Arguments {
        @Mixin
        JdbcParametersDelegate jdbcParams = new JdbcParametersDelegate();

        @Override
        public boolean hjelp() {
            return false;
        }
    }
}
