package com.github.crehn.listquery;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Value;

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
