package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;
import java.util.Optional;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;
import org.junit.Test;

/**
 * @author Snorre E. Brekke - Computas
 */
public class ArgumentSummaryTest {
    @Test
    public void testArgumentSummaryForDefaultArguments() throws Exception {
        TestParameters programArguments = new TestParameters();
        programArguments.required = "req";
        programArguments.setPrivateOptional("opt");;
        String parameterSummary = ArgumentSummary.createParameterSummary(programArguments);
        assertThat(parameterSummary).contains("-r: " + programArguments.required);
        assertThat(parameterSummary).contains("-o: " + programArguments.getPrivateOptional());
        assertThat(parameterSummary).contains("-d: " + programArguments.duration);
        assertThat(parameterSummary).contains("-t: " + programArguments.time);
        assertThat(parameterSummary).doesNotContain("-help");
        assertThat(parameterSummary).doesNotContain("-url");
        assertThat(parameterSummary).doesNotContain("-pw");
    }

    @Test
    public void testArgumentSummaryForOptionalArguments() throws Exception {
        TestParameters programArguments = new TestParameters();
        programArguments.url = Optional.of("url");
        programArguments.password = Optional.of(Paths.get("wherever"));
        String parameterSummary = ArgumentSummary.createParameterSummary(programArguments);
        assertThat(parameterSummary).contains("-url: url");
        assertThat(parameterSummary).contains("-pw: wherever");
    }

    @Test
    public void testArgumentSummaryForDelegate() throws Exception {
        RecursiveDelegates programArguments = new RecursiveDelegates();
        String parameterSummary = ArgumentSummary.createParameterSummary(programArguments);
        assertThat(parameterSummary).contains("-p");
        assertThat(parameterSummary).contains("-x");
        assertThat(parameterSummary).doesNotContain("delegate");
    }

    public static class RecursiveDelegates implements Arguments{
        @ParametersDelegate
        Delegate1 delegate = new Delegate1();

        @Override
        public boolean hjelp() {
            return false;
        }
    }

    public static class Delegate1{
        @Parameter(names ="-p")
        boolean param;

        @ParametersDelegate
        Delegate2 delegate = new Delegate2();
    }

    public static class Delegate2{
        @Parameter(names ="-x")
        boolean param;
    }
}