package no.spk.tidsserie.input;

import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

/**
 * Brukes av {@link ProgramArgumentsFactory} for å utføre postparse-validering.
 */
@FunctionalInterface
public interface PostParseValidator<T> {
    /**
     * Validerer {@code programArguments}, og kaster {@link ParameterException} ved feil.
     * @param programArguments Programargumentet som skal valideres
     * @param spec Kommandospesifikasjonen fra kommandoobjektet (picocli)
     * @throws ParameterException dersom {@code programArguments} inneholder feil
     */
    void validate(T programArguments, final CommandSpec spec) throws ParameterException;
}
