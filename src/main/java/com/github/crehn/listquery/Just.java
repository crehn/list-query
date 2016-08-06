package com.github.crehn.listquery;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Just {

    public static <T, U> List<U> map(Collection<T> list, Function<T, U> mapper) {
        return list.stream() //
                .map(mapper) //
                .collect(toList());
    }

    public static <T> List<T> filter(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .filter(predicate) //
                .collect(toList());
    }

    public static <T> String join(Collection<T> list, String delimiter) {
        return list.stream() //
                .map(Object::toString) //
                .collect(joining(delimiter));
    }

    public static <T> boolean allAre(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .allMatch(predicate);
    }

    public static <T> boolean oneExists(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .anyMatch(predicate);
    }

    public static <T> boolean noneIs(Collection<T> list, Predicate<T> predicate) {
        return list.stream() //
                .noneMatch(predicate);
    }
}
