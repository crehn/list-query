package com.github.crehn.listquery;

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
}
