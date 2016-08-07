package com.github.crehn.listquery;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Specifies paging of the result of a list query. The first page is page 1 (not page 0). Each page has at most
 * {@link #perPage} elements. Construct instances using {@link #page(int)}. Example:
 *
 * <pre>
 * <code>
 * page(2).eachContaining(10);
 * </code>
 * </pre>
 */
@Value
@AllArgsConstructor(access = PRIVATE)
public class Paging {

    int page;
    int perPage;

    public static OngoingPaging page(int page) {
        return new OngoingPaging(page);
    }

    @Value
    @AllArgsConstructor(access = PRIVATE)
    public static class OngoingPaging {
        int page;

        public Paging eachContaining(int perPage) {
            return new Paging(page, perPage);
        }
    }
}
