package no.spk.faktura.input;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * Util-klasse med metoden {@link #create} som produserer @{link T} fra en array med {@code String[]}
 *
 * @author Snorre E. Brekke - Computas
 * @param <T> Typen som lages av ProgramArgumentsFactory. Klassen m� ha en no-args konstrukt�r.
 */
public class ProgramArgumentsFactory<T extends Arguments> {

    private final Class<T> programArgumentClass;
    private final Optional<PostParseValidator<T>> postValidator;

    /**
     * Oppretter et nytt ProgramArgumentsFactory uten postvalidering.
     * @param programArgumentClass Typen som ProgramArgumentsFactory skal kunne lage via {@link #create(String...)}. Klassen m� ha en no-args konstrukt�r.
     */
    public ProgramArgumentsFactory(Class<T> programArgumentClass) {
        this(programArgumentClass, null);
    }

    /**
     * Oppretter et nytt ProgramArgumentsFactory med angitt postvalidator.
     * @param programArgumentClass Typen som ProgramArgumentsFactory skal kunne lage via {@link #create(String...)}. Klassen m� ha en no-args konstrukt�r.
     * @param postValidator validator som kj�rer etter {@link JCommander} har fullf�rt validering.
     */
    public ProgramArgumentsFactory(Class<T> programArgumentClass, PostParseValidator<T> postValidator) {
        requireNonNull(programArgumentClass, "programArgumentClass kan ikke v�re null");
        this.programArgumentClass = programArgumentClass;
        this.postValidator = ofNullable(postValidator);
    }

    /**
     * Tar en array med streng-argumenter og transformerer til en @{link ProgramArguments} representasjon av disse.
     * Metoden foretar validering og parsing av argumentene.
     * <p>
     * <b>NB!</b>
     * Printer ut bruk til konsoll dersom det er feil i argumentene,
     * eller argumentene inneholder flagg som indikerer at det �nskes hjelp.
     *
     * @param args typisk hentet fra main(String... args)
     * @return ProgramArguments generert fra args, dersom de kunne opprettes. {@link Optional#empty} dersom det er valideringsfeil.
     * @see JCommander
     */
    public T create(final String... args) {
        return create(true, args);
    }

    /**
     * Transformerer argumentene til {@link T}, see {@link #create(String...)} for mer informasjon.
     * <p>
     * Denne metoden gj�r det mulig � parse uten � utf�re postvalidering av parameterne mot hverandre og er kun synlig
     * for intern bruk og tester.
     *
     * @param postValider <code>true</code> dersom postvalidering av parameterne skal uytf�res, <code>false</code> ellers
     * @param args        typisk hentet fra main(String... args)
     * @return ProgramArguments generert fra args, dersom de kunne opprettes
     * @see #create(String...)
     * @throws InvalidParameterException dersom det er feil i ett eller flere argumenter
     * @throws UsageRequestedException {@code args} inneholder parameter som indikerer at brukeren �nsker hjelp
     */
    public T create(final boolean postValider, final String... args) {
        final T arguments = createProgramArguments();

        final JCommander jCommander = new JCommander(arguments);
        try {
            jCommander.parse(args);
            if (postValider) {
                postValidator.ifPresent(p -> p.validate(arguments));
            }
        } catch (final ParameterException exception) {
            throw new InvalidParameterException(jCommander, exception);
        }

        if (arguments.hjelp()) {
            throw new UsageRequestedException(jCommander);
        }
        return arguments;
    }

    private T createProgramArguments() {
        try {
            return programArgumentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(programArgumentClass.getClass() + " mangler en tilgjengelig no-args konstrukt�r.");
        }
    }
}
