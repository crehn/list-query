package com.github.crehn.listquery;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ListQuery {

    public interface UntypedListQuery<T> extends ListQuery {
        UntypedListQueryWithWhereClause<T> where(Predicate<T> predicate);

        List<T> select();

        Optional<T> selectFirst();

        <U> Optional<U> selectFirst(Function<T, U> mapper);

        <U> List<U> select(Function<T, U> mapper);

        <U> TypedListQuery<T, U> orderBy(Comparator<U> comparator);

        UntypedListQuery<T> ordered();

        <U, V extends Comparable<V>> TypedListQuery<T, U> orderBy(Function<U, V> getter);
    }

    public interface UntypedListQueryWithWhereClause<T>
            extends UntypedListQuery<T>, ListQueryWithWhereClause<T, UntypedListQuery<T>> {
    }

    interface ListQueryWithWhereClause<T, Q> {
        Q and(Predicate<T> predicate);

        Q or(Predicate<T> predicate);
    }

    public interface TypedListQuery<T, U> extends ListQuery {
        Optional<U> selectFirst(Function<T, U> mapper);

        List<U> select(Function<T, U> mapper);
    }

    public interface TypedListQueryWithOneType<T> extends ListQuery {
        Optional<T> selectFirst();

        Optional<T> selectFirst(Function<T, T> mapper);

        List<T> select(Function<T, T> mapper);
    }
}