package no.spk.faktura.input;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import picocli.CommandLine.Mixin;

import java.nio.file.Path;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class JdbcParametersDelegateTest {

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testFactoryForJdbcDelegateForTomArgs() {
        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
        final TestArgument testArgument = factory.create();
        assertThat(testArgument.jdbcParams.jdbcUrl.isPresent()).isFalse();
        assertThat(testArgument.jdbcParams.jdbcBrukernavn.isPresent()).isFalse();
        assertThat(testArgument.jdbcParams.jdbcPassordfil.isPresent()).isFalse();
    }

    @Test
    public void testFactoryForJdbcDelegateParsesArgs() throws Exception {
        final Path expected = temp.newFile().toPath();

        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
        final TestArgument testArgument = factory.create(
                "-jdbcUrl", "jdbc:jtds:sybase://server:port/database",
                "-jdbcBrukernavn", "user",
                "-jdbcPassordfil", expected.toString());
        assertThat(testArgument.jdbcParams.getJdbcUrl()).isEqualTo(of("jdbc:jtds:sybase://server:port/database"));
        assertThat(testArgument.jdbcParams.getJdbcBrukernavn()).isEqualTo(of("user"));
        assertThat(testArgument.jdbcParams.getJdbcPassordfil()).isEqualTo(of(expected));
    }

    @Test
    public void testFactoryForJdbcDelegateSkalValidereUrl() throws Exception {
        final Path expected = temp.newFile().toPath();
        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> factory.create(
                        "-jdbcUrl", "feilurl",
                        "-jdbcBrukernavn", "user",
                        "-jdbcPassordfil", expected.toString())
        );
        assertTrue(invalidParameterException.getMessage().contains("Feil i parameter: Parameter -jdbcUrl må inneholde en gyldig JDBC-url på formen " +
                "'jdbc:jtds:sybase://<server>:<port>/<database>' eller 'jdbc:jtds:sqlserver://<server>:<port>/<database>', du sendte inn feilurl"));
    }

    @Test
    public void testFactoryForJdbcDelegateSkalValiderePassordfilsti() {
        final ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> factory.create(
                        "-jdbcUrl", "jdbc:jtds:sybase://server:port/database",
                        "-jdbcBrukernavn", "user",
                        "-jdbcPassordfil", "missing")
        );
        assertTrue(invalidParameterException.getMessage().contains("missing eksisterer ikke"));
    }

    public static class TestArgument implements Arguments {
        @Mixin
        JdbcParametersDelegate jdbcParams = new JdbcParametersDelegate();

        @Override
        public boolean hjelp() {
            return false;
        }
    }
}
