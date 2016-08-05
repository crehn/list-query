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
    private List<T> list;
    private Predicate<T> where = e -> true;
    private Comparator<U> comparator = null;

    public TypedListQueryImpl(ListQueryImpl<T> untypedQuery) {
        this.list = untypedQuery.getList();
        this.where = untypedQuery.getWhere();
    }

    @Override
    public List<U> select(Function<T, U> mapper) {
        return selectStream(mapper) //
                .collect(toList());
    }

    @Override
    public Optional<U> selectFirst(Function<T, U> mapper) {
        return selectStream(mapper) //
                .findFirst();
    }

    private Stream<U> selectStream(Function<T, U> mapper) {
        Stream<U> result = list.stream() //
                .filter(where) //
                .map(mapper);
        return comparator != null ? result.sorted(comparator) : result;
    }

}
