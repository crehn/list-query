package com.github.crehn.listquery;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This grammar defines the allowed queries:
 *
 * <pre>
 * <code>
 * ListQuery ::= FROM + [WHERE] + [".ordered()"] + [SPECIAL] + SELECT
 *             | FROM + [WHERE] +    ORDER_BY    + [SPECIAL] + MAP_SELECT ;
 * FROM ::= "from(collection)" ;
 * WHERE ::= ".where(predicate)" + [AND_OR] ;
 * AND_OR ::= { ".and(predicate)" | ".or(predicate)" } ;
 * ORDER_BY ::= ".orderBy(comparator)" | ".orderBy(getter)" ;
 * SPECIAL ::= { [".limit(limit)"] + [".distinct()"] } ;
 * SELECT ::= MAP_SELECT | IDENTITY_SELECT ;
 * IDENTITY_SELECT ::= ".select()" | ".select(paging)" | ".selectFirst()" ;
 * MAP_SELECT ::= ".select(mapper)" | ".select(mapper, paging)" | ".selectFirst(mapper)" ;
 * </code>
 * </pre>
 *
 * The grammar above is equivalent to the grammar below which is directly realized by the corresponding interfaces.
 *
 * <pre>
 * <code>
 * ListQuery ::= ListQueryWithFrom ;
 * ListQueryWithFrom ::= WHERE + ListQueryWithWhere
 *                      | ".ordered()" + ListQueryWithOrderBy
 *                      | ORDER_BY + TypedListQueryWithOrderBy
 *                      | SPECIAL + ListQueryWithSpecial
 *                      | SELECT ;
 * ListQueryWithWhere ::= ".ordered()" + ListQueryWithOrderBy
 *                      | ORDER_BY + TypedListQueryWithOrderBy
 *                      | SPECIAL + ListQueryWithSpecial
 *                      | SELECT ;
 * ListQueryWithOrderBy ::= SPECIAL + ListQueryWithSpecial
 *                        | SELECT ;
 * TypedListQueryWithOrderBy ::= SPECIAL + TypedListQueryWithSpecial
 *                             | MAP_SELECT
 * TypedListQueryWithSpecial ::= MAP_SELECT
 * </code>
 * </pre>
 */
public interface ListQuery {

    interface ListQueryWithFrom<T> extends //
            ListQueryOrderBys<T>, //
            ListQuerySpecials<T>, //
            ListQuerySelects<T> //
    {
        ListQueryWithWhere<T> where(Predicate<T> predicate);
    }

    interface ListQueryWithWhere<T> extends //
            ListQueryOrderBys<T>, //
            ListQuerySpecials<T>, //
            ListQuerySelects<T> //
    {
        ListQueryWithWhere<T> and(Predicate<T> predicate);

        ListQueryWithWhere<T> or(Predicate<T> predicate);
    }

    interface ListQueryWithOrderBy<T> extends //
            ListQuerySpecials<T>, //
            ListQuerySelects<T> //
    {
    }

    interface ListQueryWithSpecial<T> extends //
            ListQuerySelects<T> //
    {
    }

    interface TypedListQueryWithOrderBy<T, U> extends //
            TypedListQuerySpecials<T, U>, //
            TypedListQuerySelects<T, U> //
    {
    }

    interface TypedListQueryWithSpecial<T, U> extends //
            TypedListQuerySelects<T, U> //
    {
    }


    // "mixin" interfaces

    interface ListQueryOrderBys<T> {
        ListQueryWithOrderBy<T> ordered();

        <U, V extends Comparable<V>> TypedListQueryWithOrderBy<T, U> orderBy(Function<U, V> getter);

        <U> TypedListQueryWithOrderBy<T, U> orderBy(Comparator<U> comparator);
    }

    interface ListQuerySpecials<T> {
        ListQueryWithOrderBy<T> distinct();

        ListQueryWithOrderBy<T> limit(long limit);
    }

    interface ListQuerySelects<T> {
        List<T> select();

        List<T> select(Paging paging);

        <U> List<U> select(Function<T, U> mapper);

        <U> List<U> select(Function<T, U> mapper, Paging paging);

        Optional<T> selectFirst();

        <U> Optional<U> selectFirst(Function<T, U> mapper);
    }

    interface TypedListQuerySpecials<T, U> {
        TypedListQueryWithOrderBy<T, U> distinct();

        TypedListQueryWithOrderBy<T, U> limit(long limit);
    }

    interface TypedListQuerySelects<T, U> {

        List<U> select(Function<T, U> mapper);

        List<U> select(Function<T, U> mapper, Paging paging);

        Optional<U> selectFirst(Function<T, U> mapper);
    }
}
