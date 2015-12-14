package no.spk.faktura.input;

import com.beust.jcommander.ParameterException;

/**
 * Brukes av {@link ProgramArgumentsFactory} for å utføre postparse-validering.
 * @author Snorre E. Brekke - Computas
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
