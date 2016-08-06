package com.github.crehn.listquery;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.crehn.listquery.ListQuery.TypedListQuery;

import lombok.*;
import lombok.experimental.Wither;

/**
 * immutable
 */
@Wither(PACKAGE)
@AllArgsConstructor(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class TypedListQueryImpl<T, U> implements TypedListQuery<T, U> {

    @NonNull
    private Collection<T> list;
    private Predicate<T> where;
    private Comparator<U> comparator;
    private boolean distinct = false;
    private long limit = Long.MAX_VALUE;

    public TypedListQueryImpl(ListQueryImpl<T> untypedQuery) {
        this.list = untypedQuery.getList();
        this.where = untypedQuery.getWhere();
    }

    @Override
    public TypedListQuery<T, U> distinct() {
        return this.withDistinct(true);
    }

    @Override
    public TypedListQuery<T, U> limit(long limit) {
        return this.withLimit(limit);
    }

    @Override
    public List<U> select(Function<T, U> mapper) {
        return select(mapper, null);
    }

    @Override
    public List<U> select(Function<T, U> mapper, Paging paging) {
        return selectStream(mapper, paging) //
                .collect(toList());
    }

    private Stream<U> selectStream(Function<T, U> mapper, Paging paging) {
        Stream<U> result = list.stream() //
                .filter(where) //
                .limit(limit) //
                .map(mapper);
        result = distinct ? result.distinct() : result;
        result = applyPaging(result, paging);
        return comparator != null ? result.sorted(comparator) : result;
    }


    private Stream<U> applyPaging(Stream<U> result, Paging paging) {
        if (paging == null)
            return result;

        return result //
                .skip((paging.getPage() - 1) * paging.getPerPage()) //
                .limit(paging.getPerPage());
    }

    @Override
    public Optional<U> selectFirst(Function<T, U> mapper) {
        return selectStream(mapper, null) //
                .findFirst();
    }

}
