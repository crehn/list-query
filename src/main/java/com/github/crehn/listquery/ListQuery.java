package com.github.crehn.listquery;

import static java.util.Arrays.asList;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * Create a list query which reads from the given collection. Using list query will never change the given
     * collection itself but rather return a new List.
     */
    static <T> ListQueryWithFrom<T> from(Collection<T> list) {
        return new ListQueryImpl<>(list);
    }

    /**
     * Create a list query which reads from the given array. Using list query will never change the array itself but
     * rather return a new List.
     *
     * @implNote This method converts the array to a list, and does not operate on the array directly, so there is some
     *           performance cost.
     */
    @SafeVarargs
    static <T> ListQueryWithFrom<T> from(T... array) {
        return new ListQueryImpl<>(asList(array));
    }

    interface ListQueryWithFrom<T> extends //
            ListQueryOrderBys<T>, //
            ListQuerySpecials<T>, //
            ListQuerySelects<T> //
    {
        /**
         * Filter the collection based on the given predicate. Filtering takes place before mapping to a new type (which
         * is defined in the select clause). If you need to filter after the mapping, use {@link Stream} instead.
         */
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
        /**
         * Sort the result based on the natural order defined by implementing Comparable. The result of the query is
         * sorted (i.e. not the source). So when you specify a mapping to a new type in the select clause, that new type
         * needs to implement Comparable rather than the type of the source list. If you need to sort based on the
         * source list, use {@link Stream} instead.
         *
         * <code>select()</code> will throw a ClassCastException if the resulting type is not comparable.
         *
         * @see Stream#sorted()
         * @see ListQuerySelects
         * @see TypedListQuerySelects
         */
        ListQueryWithOrderBy<T> ordered();

        /**
         * Sort the result by the given field. the sort oder is based on the natural order defined by implementing
         * Comparable. The result of the query is sorted (i.e. not the source). So when you specify a mapping to a new
         * type in the select clause, that new type needs to have the given field rather than the type of the source
         * list. If you need to sort based on the source list, use {@link Stream} instead.
         *
         * Example:
         *
         * <pre>
         * <code>
         * Name alice = new Name("Alice", "Liddell");
         * Name bob = new Name("Bob", "Dylan");
         * Name clara = new Name("Clara", "Oswald");
         * List&lt;Name&gt; nameList = asList(alice, bob, clara);
         *
         * List&lt;Name&gt; result = from(nameList) //
         *         .orderBy(Name::getLastName) //
         *         .select(e -&gt; e);
         * </code>
         * </pre>
         *
         * @param getter
         *            Supply a reference to a getter method that is used for comparison. The type of the getter method
         *            needs to be {@link Comparable}.
         * @see Stream#sorted(Comparator)
         */
        <U, V extends Comparable<V>> TypedListQueryWithOrderBy<T, U> orderBy(Function<U, V> getter);

        /**
         * Sort the result based on the given comparator. The result of the query is sorted (i.e. not the source). So
         * when you specify a mapping to a new type in the select clause, the comparator needs to be able to compare
         * that type rather than the type of the source list. If you need to sort based on the source list, use
         * {@link Stream} instead.
         *
         * @see Stream#sorted(Comparator)
         */
        <U> TypedListQueryWithOrderBy<T, U> orderBy(Comparator<U> comparator);
    }

    interface ListQuerySpecials<T> {
        /**
         * Remove duplicates from the list.
         *
         * @see Stream#distinct()
         */
        ListQueryWithOrderBy<T> distinct();

        /**
         * Limit the result to the given amount. Additional elements are truncated. For implementing paging, you should
         * rather use the paging parameter in the select method.
         *
         * @see Stream#limit(long)
         * @see ListQuerySelects#select(Paging)
         * @see ListQuerySelects#select(Function, Paging)
         * @see TypedListQuerySelects#select(Function, Paging)
         */
        ListQueryWithOrderBy<T> limit(long limit);
    }

    interface ListQuerySelects<T> {
        /**
         * Get the resulting list of the query. The source list is not changed but rather a new list is constructed.
         *
         * @see Collectors#toList()
         */
        List<T> select();

        /**
         * Get the resulting list of the query. The source list is not changed but rather a new list is constructed. The
         * paging parameter specifies which sublist to return.
         *
         * Example:
         *
         * <pre>
         * <code>
         * from(list).select(page(2).eachContaining(10));
         * </code>
         * </pre>
         *
         * @param paging
         *            Specifies only to return page {@link Paging#page} with {@link Paging#perPage} elements per page.
         *            Paging starts with page 1.
         * @see Collectors#toList()
         */
        List<T> select(Paging paging);

        /**
         * Get the resulting list of the query. The source list is not changed but rather a new list is constructed.
         * Before returning the result, apply the given mapping function. If you need earlier mapping (e.g. before
         * filtering or sorting), use {@link Stream} instead.
         *
         * @see Collectors#toList()
         * @see Stream#map(Function)
         */
        <U> List<U> select(Function<T, U> mapper);


        /**
         * Get the resulting list of the query. The source list is not changed but rather a new list is constructed.
         * Before returning the result, apply the given mapping function. If you need earlier mapping (e.g. before
         * filtering or sorting), use {@link Stream} instead. The paging parameter specifies which sublist to return.
         *
         * Example:
         *
         * <pre>
         * <code>
         * from(list).select(Integer::toString, page(2).eachContaining(10));
         * </code>
         * </pre>
         *
         * @param paging
         *            Specifies only to return page {@link Paging#page} with {@link Paging#perPage} elements per page.
         *            Paging starts with page 1.
         * @see Collectors#toList()
         * @see Stream#map(Function)
         */
        <U> List<U> select(Function<T, U> mapper, Paging paging);

        Optional<T> selectFirst();


        /**
         * Get the first element that satisfies the query. Before returning the result, apply the given mapping
         * function. If you need earlier mapping (e.g. before filtering or sorting), use {@link Stream} instead.
         *
         * @see Stream#findFirst()
         * @see Stream#map(Function)
         */
        <U> Optional<U> selectFirst(Function<T, U> mapper);
    }

    interface TypedListQuerySpecials<T, U> {
        /**
         * Remove duplicates from the list.
         *
         * @see Stream#distinct()
         */
        TypedListQueryWithOrderBy<T, U> distinct();

        /**
         * Limit the result to the given amount. Additional elements are truncated. For implementing paging, you should
         * rather use the paging parameter in the select method.
         *
         * @see Stream#limit(long)
         * @see ListQuerySelects#select(Paging)
         * @see ListQuerySelects#select(Function, Paging)
         * @see TypedListQuerySelects#select(Function, Paging)
         */
        TypedListQueryWithOrderBy<T, U> limit(long limit);
    }

    interface TypedListQuerySelects<T, U> {
        /**
         * Get the resulting list of the query. The source list is not changed but rather a new list is constructed.
         * Before returning the result, apply the given mapping function. If you need earlier mapping (e.g. before
         * filtering or sorting), use {@link Stream} instead.
         *
         * @see Collectors#toList()
         * @see Stream#map(Function)
         */
        List<U> select(Function<T, U> mapper);

        /**
         * Get the resulting list of the query. The source list is not changed but rather a new list is constructed.
         * Before returning the result, apply the given mapping function. If you need earlier mapping (e.g. before
         * filtering or sorting), use {@link Stream} instead. The paging parameter specifies which sublist to return.
         *
         * Example:
         *
         * <pre>
         * <code>
         * from(list).select(Integer::toString, page(2).eachContaining(10));
         * </code>
         * </pre>
         *
         * @param paging
         *            Specifies only to return page {@link Paging#page} with {@link Paging#perPage} elements per page.
         *            Paging starts with page 1.
         * @see Collectors#toList()
         * @see Stream#map(Function)
         */
        List<U> select(Function<T, U> mapper, Paging paging);

        /**
         * Get the first element that satisfies the query. Before returning the result, apply the given mapping
         * function. If you need earlier mapping (e.g. before filtering or sorting), use {@link Stream} instead.
         *
         * @see Stream#findFirst()
         * @see Stream#map(Function)
         */
        Optional<U> selectFirst(Function<T, U> mapper);
    }
}
