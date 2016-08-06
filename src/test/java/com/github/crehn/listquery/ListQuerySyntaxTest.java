package com.github.crehn.listquery;

import static java.util.Arrays.asList;

import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class ListQuerySyntaxTest {

    @SuppressWarnings("unused")
    private List<Integer> list = asList(1, 2, 3, 4, 5);
    @SuppressWarnings("unused")
    private Comparator<Integer> comparator = (Integer e1, Integer e2) -> -1 * e1.compareTo(e2);

    /**
     * when uncommenting these lines each line should raise a compiler error
     */
    @Test
    public void shouldNotCompile() {
        // from(list).where(e -> e > 1).where(e -> e > 1).select();
        //
        // from(list).and(e -> e > 1).select(e -> e);
        // from(list).or(e -> e > 1).select(e -> e);
        //
        // from(list).orderBy(comparator).orderBy(comparator).select(e -> e);
        // from(list).orderBy(comparator).orderBy(Integer::intValue).select(e -> e);
        // from(list).orderBy(comparator).ordered().select(e -> e);
        // from(list).orderBy(comparator).where(e -> e > 1).select(e -> e);
        // from(list).orderBy(comparator).and(e -> e > 1).select(e -> e);
        // from(list).orderBy(comparator).or(e -> e > 1).select(e -> e);
        // from(list).orderBy(comparator).ordered().select(e -> e);
        //
        // from(list).orderBy(Integer::intValue).orderBy(comparator).select(e -> e);
        // from(list).orderBy(Integer::intValue).orderBy(Integer::intValue).select(e -> e);
        // from(list).orderBy(Integer::intValue).ordered().select(e -> e);
        // from(list).orderBy(Integer::intValue).where(e -> e > 1).select(e -> e);
        // from(list).orderBy(Integer::intValue).and(e -> e > 1).select(e -> e);
        // from(list).orderBy(Integer::intValue).or(e -> e > 1).select(e -> e);
        // from(list).orderBy(Integer::intValue).ordered().select(e -> e);
        //
        // from(list).ordered().orderBy(comparator).select(e -> e);
        // from(list).ordered().orderBy(Integer::intValue).select(e -> e);
        // from(list).ordered().ordered().select(e -> e);
        // from(list).ordered().where(e -> e > 1).select(e -> e);
        // from(list).ordered().and(e -> e > 1).select(e -> e);
        // from(list).ordered().or(e -> e > 1).select(e -> e);
        // from(list).ordered().ordered().select(e -> e);
        //
        // from(list).distinct().where(e -> e > 1).select(e -> e);
        // from(list).distinct().and(e -> e > 1).select(e -> e);
        // from(list).distinct().or(e -> e > 1).select(e -> e);
        // from(list).distinct().orderBy(Integer::intValue).select(e -> e);
        // from(list).distinct().orderBy(comparator).select(e -> e);
        // from(list).distinct().ordered().select(e -> e);
        // from(list).distinct().distinct().select(e -> e);
        //
        // from(list).limit(1).where(e -> e > 1).select(e -> e);
        // from(list).limit(1).and(e -> e > 1).select(e -> e);
        // from(list).limit(1).or(e -> e > 1).select(e -> e);
        // from(list).limit(1).orderBy(Integer::intValue).select(e -> e);
        // from(list).limit(1).orderBy(comparator).select(e -> e);
        // from(list).limit(1).ordered().select(e -> e);
        // from(list).limit(1).limit(1).select(e -> e);
    }

}
