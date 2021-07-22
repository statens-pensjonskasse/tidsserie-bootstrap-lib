package no.spk.faktura.input;

import java.util.stream.Collectors;

public final class ArgumentSummary {

    private ArgumentSummary() {
    }

    public static String createParameterSummary(final PrintbareProgramargumenter programArguments) {
        return
                programArguments.argumenter()
                        .stream()
                        .map(s -> String.format("\n- %s", s))
                        .collect(Collectors.joining(""))
                        + "\n\n";
    }
}
