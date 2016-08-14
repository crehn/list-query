package com.github.crehn.listquery;

import static com.github.crehn.listquery.Just.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class JustTest {

    private Integer[] array = new Integer[] { 1, 2, 3, 4, 5 };
    private List<Integer> list = asList(array);

    @Test
    public void shouldJustMap() {
        List<Integer> result = map(list, e -> e + 1);

        assertEquals(asList(2, 3, 4, 5, 6), result);
    }

    @Test
    public void shouldJustMapArray() {
        List<Integer> result = map(array, e -> e + 1);

        assertEquals(asList(2, 3, 4, 5, 6), result);
    }

    @Test
    public void shouldJustFilter() {
        List<Integer> result = filter(list, e -> e > 2);

        assertEquals(asList(3, 4, 5), result);
    }

    @Test
    public void shouldJustFilterArray() {
        List<Integer> result = filter(array, e -> e > 2);

        assertEquals(asList(3, 4, 5), result);
    }

    @Test
    public void shouldJustJoin() {
        String result = join(list, ", ");

        assertEquals("1, 2, 3, 4, 5", result);
    }

    @Test
    public void shouldJustJoinArray() {
        String result = join(array, ", ");

        assertEquals("1, 2, 3, 4, 5", result);
    }

    @Test
    public void shouldJustCheckThatAllAre() {
        assertEquals(true, allAre(list, e -> e < 10));
        assertEquals(false, allAre(list, e -> e % 2 == 0));
        assertEquals(false, allAre(list, e -> e > 10));
    }

    @Test
    public void shouldJustCheckThatInArrayAllAre() {
        assertEquals(true, allAre(array, e -> e < 10));
        assertEquals(false, allAre(array, e -> e % 2 == 0));
        assertEquals(false, allAre(array, e -> e > 10));
    }

    @Test
    public void shouldJustCheckThatOneExists() {
        assertEquals(true, oneExists(list, e -> e < 10));
        assertEquals(true, oneExists(list, e -> e % 2 == 0));
        assertEquals(false, oneExists(list, e -> e > 10));
    }

    @Test
    public void shouldJustCheckThatInArrayOneExists() {
        assertEquals(true, oneExists(array, e -> e < 10));
        assertEquals(true, oneExists(array, e -> e % 2 == 0));
        assertEquals(false, oneExists(array, e -> e > 10));
    }

    @Test
    public void shouldJustCheckThatNoneIs() {
        assertEquals(false, noneIs(list, e -> e < 10));
        assertEquals(false, noneIs(list, e -> e % 2 == 0));
        assertEquals(true, noneIs(list, e -> e > 10));
    }

    @Test
    public void shouldJustCheckThatInArrayNoneIs() {
        assertEquals(false, noneIs(array, e -> e < 10));
        assertEquals(false, noneIs(array, e -> e % 2 == 0));
        assertEquals(true, noneIs(array, e -> e > 10));
    }
}
