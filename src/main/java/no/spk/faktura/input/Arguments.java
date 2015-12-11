package no.spk.faktura.input;

/**
 * Interface som må implementeres av klasser som an ønsker å bruke sammen med {@link ProgramArgumentsFactory}
 * @author Snorre E. Brekke - Computas
 */
public interface Arguments {
    /**
     * Angir om brukeren har angitt hjelp-parameter fra kommandolinjen.
     * @return true dersom hjelp er angitt
     */
    boolean hjelp();
}
