package com.github.crehn.listquery;

import static com.github.crehn.listquery.ListQuery.from;
import static com.github.crehn.listquery.Paging.page;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;

import lombok.Value;

public class ListQueryTest {

    private List<Integer> list = asList(1, 2, 3, 4, 5);

    @Test
    public void shouldSelect() {
        List<Integer> result = from(list).select();

        assertEquals(asList(1, 2, 3, 4, 5), result);
    }

    @Test
    public void shouldMapAndSelect() {
        List<Integer> result = from(list).select(e -> e + 1);

        assertEquals(asList(2, 3, 4, 5, 6), result);
    }

    @Test
    public void shouldSelectFirst() {
        Optional<Integer> result = from(list).selectFirst();

        assertEquals(1, (int) result.get());
    }

    @Test
    public void shouldMapAndSelectFirst() {
        Optional<Integer> result = from(list).selectFirst(e -> e + 1);

        assertEquals(2, (int) result.get());
    }

    @Test
    public void shouldSelectDistinct() {
        List<Integer> result = from(list) //
                .distinct() //
                .select(e -> e / 2);

        assertEquals(asList(0, 1, 2), result);
    }

    @Test
    public void shouldFilter() {
        List<Integer> result = from(list).where(e -> e > 2).select();

        assertEquals(asList(3, 4, 5), result);
    }

    @Test
    public void shouldFilterTwiceUsingAnd() {
        List<Integer> result = from(list) //
                .where(e -> e > 2) //
                .and(e -> e <= 4) //
                .select();

        assertEquals(asList(3, 4), result);
    }

    @Test
    public void shouldFilterTwiceUsingOr() {
        List<Integer> result = from(list) //
                .where(e -> e < 2) //
                .or(e -> e > 4) //
                .select();

        assertEquals(asList(1, 5), result);
    }

    @Test
    public void shouldOrderUsingComparator() {
        List<Integer> result = from(list) //
                .orderBy((Integer e1, Integer e2) -> -1 * e1.compareTo(e2)) //
                .select(e -> e);

        assertEquals(asList(5, 4, 3, 2, 1), result);
    }

    @Test
    public void shouldOrderNaturally() {
        Collections.shuffle(list);
        // assertNotEquals(asList(1, 2, 3, 4, 5), list);

        List<Integer> result = from(list) //
                .ordered() //
                .select();

        assertEquals(asList(1, 2, 3, 4, 5), result);
    }

    @Test
    public void shouldOrderByField() {
        Name alice = new Name("Alice", "Liddell");
        Name bob = new Name("Bob", "Dylan");
        Name clara = new Name("Clara", "Oswald");
        List<Name> nameList = asList(alice, bob, clara);

        List<Name> result = from(nameList) //
                .orderBy(Name::getLastName) //
                .select(e -> e);

        assertEquals(asList(bob, alice, clara), result);
    }

    @Value
    private static class Name {
        String firstName;
        String lastName;
    }

    @Test
    public void shouldLimit() {
        List<Integer> result = from(list) //
                .limit(3) //
                .select();

        assertEquals(asList(1, 2, 3), result);
    }

    @Test
    public void shouldPage() {
        List<Integer> result = from(list) //
                .select(page(2).eachContaining(2));

        assertEquals(asList(3, 4), result);
    }

    @Test
    public void shouldMapAndPage() {
        List<Integer> result = from(list) //
                .select(e -> e + 1, page(2).eachContaining(2));

        assertEquals(asList(4, 5), result);
    }

}
