package no.spk.faktura.input;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;

/**
 * Util-klasse med metoden {@link #create} som produserer @{link T} fra en array med {@code String[]}
 *
 * @param <T> Typen som lages av ProgramArgumentsFactory. Klassen må ha en no-args konstruktør.
 */
public class ProgramArgumentsFactory<T extends Arguments> {

    private final Class<T> programArgumentClass;
    private final Optional<PostParseValidator<T>> postValidator;

    /**
     * Oppretter et nytt ProgramArgumentsFactory uten postvalidering.
     *
     * @param programArgumentClass Typen som ProgramArgumentsFactory skal kunne lage via {@link #create(String...)}. Klassen må ha en no-args konstruktør.
     */
    public ProgramArgumentsFactory(Class<T> programArgumentClass) {
        this(programArgumentClass, null);
    }

    /**
     * Oppretter et nytt ProgramArgumentsFactory med angitt postvalidator.
     *
     * @param programArgumentClass Typen som ProgramArgumentsFactory skal kunne lage via {@link #create(String...)}. Klassen må ha en no-args konstruktør.
     * @param postValidator validator som kjører etter picocli har fullført validering.
     */
    public ProgramArgumentsFactory(Class<T> programArgumentClass, PostParseValidator<T> postValidator) {
        requireNonNull(programArgumentClass, "programArgumentClass kan ikke være null");
        this.programArgumentClass = programArgumentClass;
        this.postValidator = ofNullable(postValidator);
    }

    /**
     * Tar en array med streng-argumenter og transformerer til en @{link ProgramArguments} representasjon av disse.
     * Metoden foretar validering og parsing av argumentene.
     * <p>
     *
     * @param args typisk hentet fra main(String... args)
     * @return ProgramArguments generert fra args, dersom de kunne opprettes. {@link Optional#empty} dersom det er valideringsfeil.
     * @throws InvalidParameterException dersom det er feil i ett eller flere argumenter
     * @throws UsageRequestedException {@code args} inneholder parameter som indikerer at brukeren ønsker hjelp
     */
    public T create(final String... args) {
        return create(true, args);
    }

    /**
     * Transformerer argumentene til {@link T}, see {@link #create(String...)} for mer informasjon.
     * <p>
     * Denne metoden gjør det mulig å parse uten å utføre postvalidering av parameterne mot hverandre og er kun synlig
     * for intern bruk og tester.
     *
     * @param postValider <code>true</code> dersom postvalidering av parameterne skal uytføres, <code>false</code> ellers
     * @param args typisk hentet fra main(String... args)
     * @return ProgramArguments generert fra args, dersom de kunne opprettes
     * @throws InvalidParameterException dersom det er feil i ett eller flere argumenter
     * @throws UsageRequestedException {@code args} inneholder parameter som indikerer at brukeren ønsker hjelp
     * @see #create(String...)
     */
    public T create(final boolean postValider, final String... args) {
        final T arguments = createProgramArguments();
        final CommandLine cmd = new CommandLine(arguments);

        try {
            final ParseResult result = cmd.parseArgs(args);

            if (result.isUsageHelpRequested()) {
                throw new UsageRequestedException(usage(cmd));
            }

            if (result.subcommand() != null && result.subcommand().isUsageHelpRequested()) {
                throw new UsageRequestedException(usage(result.subcommand().commandSpec().commandLine()));
            }

            if (postValider) {
                postValidator.ifPresent(p -> p.validate(arguments, cmd.getCommandSpec()));
            }
        } catch (final ParameterException exception) {
            throw new InvalidParameterException(usage(cmd), exception);
        }

        return arguments;
    }

    private T createProgramArguments() {
        try {
            return programArgumentClass.getDeclaredConstructor().newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(programArgumentClass.getClass() + " mangler en tilgjengelig no-args konstruktør.");
        } catch (final NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException("Noe gikk galt under innlesing av " + programArgumentClass.getClass());
        }
    }

    public String usage(final CommandLine cmd) {
        final StringWriter writer = new StringWriter();
        final PrintWriter out = new PrintWriter(writer);
        cmd.usage(out);

        return writer.toString();
    }
}
