package no.spk.faktura.input;

import picocli.CommandLine.ParameterException;

/**
 * Brukes av {@link ProgramArgumentsFactory} for å utføre postparse-validering.
 */
@FunctionalInterface
public interface PostParseValidator<T> {
    /**
     * Validerer {@code programArguments}, og kaster {@link ParameterException} ved feil.
     *@param programArguments Programargumentet som skal valideres
     * @throws ParameterException dersom {@code programArguments} inneholder feil
     */
    void validate(T programArguments) throws ParameterException;
}
