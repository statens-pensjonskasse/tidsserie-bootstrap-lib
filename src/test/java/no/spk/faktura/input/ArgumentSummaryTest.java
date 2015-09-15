package no.spk.faktura.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;
import java.util.Optional;

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
}