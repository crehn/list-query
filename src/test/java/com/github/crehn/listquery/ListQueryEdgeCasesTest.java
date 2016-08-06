package com.github.crehn.listquery;

import static com.github.crehn.listquery.ListQuery.from;
import static com.github.crehn.listquery.Paging.page;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ListQueryEdgeCasesTest {

    private List<Integer> list = asList(1, 2, 3, 4, 5);

    @Test
    public void shouldOrderAndSelectDistinct() {
        List<Integer> result = from(list) //
                .orderBy(Integer::intValue) //
                .distinct() //
                .select(e -> e / 2);

        assertEquals(asList(0, 1, 2), result);
    }

    @Test
    public void shouldOrderAndSelectFirst() {
        Collections.shuffle(list);
        // assertNotEquals(asList(1, 2, 3, 4, 5), list);

        int result = from(list) //
                .orderBy(Integer::intValue) //
                .selectFirst(e -> e) //
                .get();

        assertEquals(1, result);
    }

    @Test
    public void shouldFilterAndOrder() {
        List<Integer> result = from(list) //
                .where(e -> e > 3) //
                .orderBy((Integer e1, Integer e2) -> -1 * e1.compareTo(e2)) //
                .select(e -> e);

        assertEquals(asList(5, 4), result);
    }

    @Test
    public void shouldOrderAndLimit() {
        List<Integer> result = from(list) //
                .orderBy(Integer::intValue) //
                .limit(3) //
                .select(e -> e);

        assertEquals(asList(1, 2, 3), result);
    }

    @Test
    public void shouldSelectDistinctAndLimit() {
        List<Integer> result = from(list) //
                .distinct() //
                .limit(3) //
                .select(e -> e / 2);

        assertEquals(asList(0, 1), result);
    }

    @Test
    public void shouldLimitAndSelectDistinct() {
        List<Integer> result = from(list) //
                .limit(3) //
                .distinct() //
                .select(e -> e / 2);

        assertEquals(asList(0, 1), result);
    }

    @Test
    public void shouldLimitTwice() {
        List<Integer> result = from(list) //
                .limit(3) //
                .limit(2) //
                .select(e -> e);

        assertEquals(asList(1, 2), result);
    }

    @Test
    public void shouldOrderAndLimitTwice() {
        List<Integer> result = from(list) //
                .orderBy(Integer::intValue) //
                .limit(3) //
                .limit(2) //
                .select(e -> e);

        assertEquals(asList(1, 2), result);
    }

    @Test
    public void shouldSortMapAndPage() {
        List<Integer> result = from(list) //
                .orderBy(Integer::intValue) //
                .select(e -> e + 1, page(2).eachContaining(2));

        assertEquals(asList(4, 5), result);
    }

    @Test
    public void shouldLimitAndPage() {
        List<Integer> result = from(list) //
                .limit(3) //
                .select(e -> e, page(2).eachContaining(2));

        assertEquals(asList(3), result);
    }

    @Test
    public void shouldOrderLimitAndPage() {
        List<Integer> result = from(list) //
                .orderBy(Integer::intValue) //
                .limit(3) //
                .select(e -> e, page(2).eachContaining(2));

        assertEquals(asList(3), result);
    }

    @Test(expected = ClassCastException.class)
    public void shouldFailOrderingTheUnorderable() {
        from(asList(new Object(), new Object())) //
                .ordered() //
                .select();
    }
}
