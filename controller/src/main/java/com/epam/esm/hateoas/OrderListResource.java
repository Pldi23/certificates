package com.epam.esm.hateoas;

import com.epam.esm.constant.EndPointConstant;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.hateoas.LinkConstant.*;

@Getter
public class OrderListResource extends ResourceSupport {

    private final List<OrderResource> orders;


    public OrderListResource(List<OrderResource> orders, int pageCurrent, long pageLast, int pageSize) {
        this.orders = orders;
        add(createLinkByTemplate(PAGE_CURRENT, pageCurrent, pageSize));
        add(createLinkByTemplate(PAGE_FIRST, 1, pageSize));
        add(createLinkByTemplate(PAGE_LAST, pageLast, pageSize));
    }

    private Link createLinkByTemplate(String pageStatus, long page, int size) {
        Link link = new Link(TEMPLATE);
        Map<String, Object> values = new HashMap<>();
        values.put(SEGMENT, EndPointConstant.ORDER_ENDPOINT);
        values.put(PAGE_TEMPLATE, page);
        values.put(SIZE_TEMPLATE, size);
        return link.withRel(pageStatus).expand(values);
    }

}
