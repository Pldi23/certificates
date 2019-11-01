package com.epam.esm.hateoas;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.constant.LinkConstant.PAGE_CURRENT;
import static com.epam.esm.constant.LinkConstant.PAGE_FIRST;
import static com.epam.esm.constant.LinkConstant.PAGE_LAST;
import static com.epam.esm.constant.LinkConstant.PAGE_TEMPLATE;
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
            links.add(createLinkByTemplate(segment, PAGE_FIRST, 1, pageSize));
        }
        if (pageCurrent > 4) {
            links.add(createLinkByTemplate(segment, String.valueOf(pageCurrent - 2), pageCurrent - 2L, pageSize));
        }
        if (pageCurrent > 3) {
            links.add(createLinkByTemplate(segment, String.valueOf(pageCurrent - 1), pageCurrent - 1L, pageSize));
        }
        links.add(createLinkByTemplate(segment, PAGE_CURRENT + " " + pageCurrent, pageCurrent, pageSize));
        if (pageCurrent < pageLast - 2) {
            links.add(createLinkByTemplate(segment, String.valueOf(pageCurrent + 1), pageCurrent + 1L, pageSize));
        }
        if (pageCurrent < pageLast - 3) {
            links.add(createLinkByTemplate(segment, String.valueOf(pageCurrent + 2), pageCurrent + 2L, pageSize));
        }
        if (pageCurrent != pageLast) {
            links.add(createLinkByTemplate(segment, PAGE_LAST, pageLast, pageSize));
        }
        return links;
    }

    private static Link createLinkByTemplate(String segment, String pageStatus, long page, int size) {
        Link link = new Link(TEMPLATE);
        Map<String, Object> values = new HashMap<>();
        values.put(SEGMENT, segment);
        values.put(PAGE_TEMPLATE, page);
        values.put(SIZE_TEMPLATE, size);
        return link.withRel(pageStatus).expand(values);
    }
}
