package no.spk.faktura.input;

import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.of;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParametersDelegate;

/**
 * Util-klasse som bruker reflection for å lage en tekstlig oppsummering av innholdet i en klasse som
 * har {@link Parameter} annoteringer på felter i klassen. Felt annotert med {@link ParametersDelegate},
 * vil rekursivt bli behandlet, og eventuelle felt annotert med {@link Parameter} under disse, vil bli inkludert i
 * oppsummeringen.
 *
 * @author Snorre E. Brekke - Computas
 */
public final class ArgumentSummary {

    private ArgumentSummary() {
    }

    /**
     * Lager en oppsummering i form av en streng som inneholder èn linje per {@link Parameter} felt i klassen.
     * <p>
     * Felt annotert med {@link ParametersDelegate},
     * vil rekursivt bli behandlet, og eventuelle felt annotert med {@link Parameter} under disse, vil bli inkludert i
     * oppsummeringen.
     * </p>
     * Følgende {@link Parameter} felt  blir utelatt fra oppsummeringen:
     * <ul>
     * <li>
     * Felt som har verdi lik {@link Optional#empty()}.
     * </li>
     * <li>
     * Felt som annotert med {@link Parameter#help()}
     * </li>
     * </ul>
     *
     * @param programArguments instans av en klasse som har en eller flere {@link Parameter} annoteringer på felter
     * @return en streng som inneholder èn linje per {@link Parameter} felt
     */
    public static String createParameterSummary(Object programArguments) {
        return parameterFields(programArguments)
                .map(f -> summary(f.field, f.instance))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private static Stream<FieldInstance> parameterFields(Object parameterObject) {
        return of(parameterObject.getClass().getDeclaredFields())
                .filter(ArgumentSummary::include)
                .map(f -> new FieldInstance(f, parameterObject))
                .flatMap(ArgumentSummary::flattenDelegates);
    }

    private static Stream<FieldInstance> flattenDelegates(FieldInstance fieldInstance) {
        if (isParameter(fieldInstance.field)) {
            return of(fieldInstance);
        } else if (isParameterDelegate(fieldInstance.field)) {
            Object delegate = getValue(fieldInstance.field, fieldInstance.instance).get();
            return parameterFields(delegate);
        }
        return empty();
    }

    private static boolean include(Field field) {
        return (isParameter(field) &&
                !field.getAnnotation(Parameter.class).help()) ||
                isParameterDelegate(field);
    }

    private static boolean isParameterDelegate(Field field) {
        return field.isAnnotationPresent(ParametersDelegate.class);
    }

    private static boolean isParameter(Field field) {
        return field.isAnnotationPresent(Parameter.class);
    }


    private static Optional<String> summary(Field field, Object instance) {
        Optional<?> value = getValue(field, instance);
        if (value.isPresent()) {
            Parameter annotation = field.getAnnotation(Parameter.class);
            String parameterString = annotation.names()[0] + ": ";
            parameterString += value.get();
            return Optional.of(parameterString);
        }
        return Optional.empty();
    }

    private static Optional<?> getValue(Field field, Object instance) {
        try {
            boolean access = field.isAccessible();
            field.setAccessible(true);
            final Object fieldValue = field.get(instance);
            field.setAccessible(access);
            return transformType(fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<?> transformType(Object fieldValue) {
        if (fieldValue instanceof Optional) {
            return (Optional) fieldValue;
        }
        return Optional.ofNullable(fieldValue);
    }

    private static class FieldInstance {
        final Field field;
        final Object instance;

        public FieldInstance(Field field, Object instance) {
            this.field = field;
            this.instance = instance;
        }
    }
}
