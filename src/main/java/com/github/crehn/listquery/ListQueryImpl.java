package com.github.crehn.listquery;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.crehn.listquery.ListQuery.UntypedListQueryWithWhereClause;

import lombok.*;
import lombok.experimental.Wither;

/**
 * immutable
 */
@Wither(PACKAGE)
@AllArgsConstructor(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class ListQueryImpl<T> implements UntypedListQueryWithWhereClause<T> {

    @NonNull
    @Getter(PACKAGE)
    private Collection<T> list;
    @Getter(PACKAGE)
    private Predicate<T> where = e -> true;
    private boolean orderedNaturally = false;
    private boolean distinct = false;
    private long limit = Long.MAX_VALUE;


    // from

    public static <T> UntypedListQuery<T> from(List<T> list) {
        return new ListQueryImpl<>(list);
    }


    // where

    @Override
    public UntypedListQueryWithWhereClause<T> where(Predicate<T> predicate) {
        return withWhere(this.where.and(predicate));
    }

    @Override
    public UntypedListQueryWithWhereClause<T> and(Predicate<T> predicate) {
        return withWhere(this.where.and(predicate));
    }

    @Override
    public UntypedListQueryWithWhereClause<T> or(Predicate<T> predicate) {
        return withWhere(this.where.or(predicate));
    }


    // order by

    @Override
    public UntypedListQuery<T> ordered() {
        return withOrderedNaturally(true);

    }

    @Override
    public <U> TypedListQuery<T, U> orderBy(Comparator<U> comparator) {
        return new TypedListQueryImpl<T, U>(this).withComparator(comparator);
    }

    @Override
    public <U, V extends Comparable<V>> TypedListQuery<T, U> orderBy(Function<U, V> getter) {
        return new TypedListQueryImpl<T, U>(this)
                .withComparator((e1, e2) -> getter.apply(e1).compareTo(getter.apply(e2)));
    }


    // distinct, limit

    @Override
    public UntypedListQuery<T> distinct() {
        return this.withDistinct(true);
    }

    @Override
    public UntypedListQuery<T> limit(long limit) {
        return this.withLimit(limit);
    }


    // select

    @Override
    public List<T> select() {
        return select(identity());
    }

    @Override
    public <U> List<U> select(Function<T, U> mapper) {
        return select(mapper, null);
    }

    @Override
    public List<T> select(Paging paging) {
        return select(identity(), paging);
    }

    @Override
    public <U> List<U> select(Function<T, U> mapper, Paging paging) {
        return selectStream(mapper, paging) //
                .collect(toList());
    }

    private <U> Stream<U> selectStream(Function<T, U> mapper, Paging paging) {
        Stream<U> result = list.stream() //
                .filter(where) //
                .limit(limit) //
                .map(mapper);
        result = distinct ? result.distinct() : result;
        result = applyPaging(result, paging);
        return orderedNaturally ? result.sorted() : result;
    }


    private <U> Stream<U> applyPaging(Stream<U> result, Paging paging) {
        if (paging == null)
            return result;

        return result //
                .skip((paging.getPage() - 1) * paging.getPerPage()) //
                .limit(paging.getPerPage());
    }

    @Override
    public Optional<T> selectFirst() {
        return selectFirst(identity());
    }

    @Override
    public <U> Optional<U> selectFirst(Function<T, U> mapper) {
        return selectStream(mapper, null).findFirst();
    }

}
