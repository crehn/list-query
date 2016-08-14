package com.github.crehn.listquery;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A utility class similar to {@link Collections} containing methods that work on collections. These methods are merely
 * shortcuts for methods in {@link Stream}. They are meant to be useful when you just want to use one specific stream
 * method. So instead of writing
 *
 * <pre>
 * <code>
 * list.stream()
 *     .map(String::toLowerCase)
 *     .collect(toList());
 * </code>
 * </pre>
 *
 * you can simply write
 *
 * <pre>
 * <code>
 * map(list, String::toLowerCase);
 * </code>
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Just {

    /**
     * Returns a new list with the given mapping function applied to each element.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     *
     * @see Stream#map(Function)
     */
    public static <T, U> List<U> map(T[] array, Function<T, U> mapper) {
        return map(asList(array), mapper);
    }

    /**
     * Returns a new list with the given mapping function applied to each element.
     *
     * @see Stream#map(Function)
     */
    public static <T, U> List<U> map(Collection<T> list, Function<T, U> mapper) {
        return list.stream() //
                .map(mapper) //
                .collect(toList());
    }

    /**
     * Returns a new list filtered using the given predicate.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     *
     * @see Stream#filter(Predicate)
     */
    public static <T> List<T> filter(T[] array, Predicate<T> predicate) {
        return filter(asList(array), predicate);
    }

    /**
     * Returns a new list filtered using the given predicate.
     *
     * @see Stream#filter(Predicate)
     */
    public static <T> List<T> filter(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .filter(predicate) //
                .collect(toList());
    }

    /**
     * Returns a string joining the string representations of each element using the given delimiter. Invokes
     * <code>toString()</code> for each element.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     *
     * @see Collectors#joining(CharSequence)
     */
    public static <T> String join(T[] array, String delimiter) {
        return join(asList(array), delimiter);
    }

    /**
     * Returns a string joining the string representations of each element using the given delimiter. Invokes
     * <code>toString()</code> for each element.
     *
     * @see Collectors#joining(CharSequence)
     */
    public static <T> String join(Collection<T> list, String delimiter) {
        return list.stream() //
                .map(Object::toString) //
                .collect(joining(delimiter));
    }

    /**
     * Returns true iff all elements in the given collection satisfy the given predicate.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     *
     * @see Stream#allMatch(Predicate)
     */
    public static <T> boolean allAre(T[] array, Predicate<T> predicate) {
        return allAre(asList(array), predicate);
    }

    /**
     * Returns true iff all elements in the given collection satisfy the given predicate.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     *
     * @see Stream#allMatch(Predicate)
     */
    public static <T> boolean allAre(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .allMatch(predicate);
    }

    /**
     * Returns true iff at least one element in the given collection satisfies the given predicate.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     *
     * @see Stream#anyMatch(Predicate)
     */
    public static <T> boolean oneExists(T[] array, Predicate<T> predicate) {
        return oneExists(asList(array), predicate);
    }

    /**
     * Returns true iff at least one element in the given collection satisfies the given predicate.
     *
     * @see Stream#anyMatch(Predicate)
     */
    public static <T> boolean oneExists(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .anyMatch(predicate);
    }

    /**
     * Returns true iff one of the elements in the given collection satisfy the given predicate.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     *
     * @see Stream#noneMatch(Predicate)
     */
    public static <T> boolean noneIs(T[] array, Predicate<T> predicate) {
        return noneIs(asList(array), predicate);
    }

    /**
     * Returns true iff one of the elements in the given collection satisfy the given predicate.
     *
     * @see Stream#noneMatch(Predicate)
     */
    public static <T> boolean noneIs(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .noneMatch(predicate);
    }
}
