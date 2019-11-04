package com.epam.esm.hateoas;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.constant.LinkConstant.PAGE_FIRST;
import static com.epam.esm.constant.LinkConstant.PAGE_LAST;
import static com.epam.esm.constant.LinkConstant.PAGE_TEMPLATE;
import static com.epam.esm.constant.LinkConstant.PAGINATION_REL;
import static com.epam.esm.constant.LinkConstant.SEGMENT;
import static com.epam.esm.constant.LinkConstant.SIZE_TEMPLATE;
import static com.epam.esm.constant.LinkConstant.TEMPLATE;

/**
 * to paginate hateoas links
 *
 * @author Dzmitry Platonov on 2019-11-01.
 * @version 0.0.1
 */
class Paginator {

    private Paginator() {
    }

    static List<Link> buildPaginationLinks(String segment, int pageCurrent, long pageLast, int pageSize) {
        List<Link> links = new ArrayList<>();
        if (pageCurrent != 1) {
            links.add(createLinkByTemplate(segment, 1, pageSize, PAGE_FIRST, false));
        }
        if (pageCurrent > 4) {
            links.add(createLinkByTemplate(segment, pageCurrent - 2L, pageSize, String.valueOf(pageCurrent - 2), false));
        }
        if (pageCurrent > 3) {
            links.add(createLinkByTemplate(segment, pageCurrent - 1L, pageSize, String.valueOf(pageCurrent - 1), false));

        }
        links.add(createLinkByTemplate(segment, pageCurrent, pageSize, String.valueOf(pageCurrent), true));
        if (pageCurrent < pageLast - 2) {
            links.add(createLinkByTemplate(segment, pageCurrent + 1L, pageSize, String.valueOf(pageCurrent + 1), false));
        }
        if (pageCurrent < pageLast - 3) {
            links.add(createLinkByTemplate(segment, pageCurrent + 2L, pageSize, String.valueOf(pageCurrent + 2), false));
        }
        if (pageCurrent != pageLast) {
            links.add(createLinkByTemplate(segment, pageLast, pageSize, PAGE_LAST, false));
        }
        return links;
    }

    private static PaginationLink createLinkByTemplate(String segment, long page, int size, String name, boolean isCurrent) {
        Link link = new Link(TEMPLATE, PAGINATION_REL);
        Map<String, Object> values = new HashMap<>();
        values.put(SEGMENT, segment);
        values.put(PAGE_TEMPLATE, page);
        values.put(SIZE_TEMPLATE, size);
        return new PaginationLink(link.expand(values), name, isCurrent);
    }
}
