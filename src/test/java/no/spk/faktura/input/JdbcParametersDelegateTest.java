package no.spk.faktura.input;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import com.beust.jcommander.ParametersDelegate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * @author Snorre E. Brekke - Computas
 */
public class JdbcParametersDelegateTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public final TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testFactoryForJdbcDelegateForTomArgs() throws Exception {
        ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
        final TestArgument testArgument = factory.create();
        assertThat(testArgument.jdbcParams.jdbcUrl.isPresent()).isFalse();
        assertThat(testArgument.jdbcParams.jdbcBrukernavn.isPresent()).isFalse();
        assertThat(testArgument.jdbcParams.jdbcPassordfil.isPresent()).isFalse();
    }

    @Test
    public void testFactoryForJdbcDelegateParsesArgs() throws Exception {
        final Path expected = temp.newFile().toPath();

        ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
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
        exception.expect(InvalidParameterException.class);
        exception.expectMessage("-jdbcUrl må inneholde en gyldig JDBC-url");
        ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
        factory.create(
                "-jdbcUrl", "feilurl",
                "-jdbcBrukernavn", "user",
                "-jdbcPassordfil", expected.toString());
    }

    @Test
    public void testFactoryForJdbcDelegateSkalValiderePassordfilsti() throws Exception {
        exception.expect(InvalidParameterException.class);
        exception.expectMessage("missing eksisterer ikke");
        ProgramArgumentsFactory<TestArgument> factory = new ProgramArgumentsFactory<>(TestArgument.class);
        factory.create(
                "-jdbcUrl", "jdbc:jtds:sybase://server:port/database",
                "-jdbcBrukernavn", "user",
                "-jdbcPassordfil", "missing");
    }

    public static class TestArgument implements Arguments{
        @ParametersDelegate
        JdbcParametersDelegate jdbcParams = new JdbcParametersDelegate();

        @Override
        public boolean hjelp() {
            return false;
        }
    }

}