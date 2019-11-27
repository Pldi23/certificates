package com.epam.esm.hateoas;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.LinkConstant;
import com.epam.esm.controller.OrderController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Getter
@EqualsAndHashCode(callSuper = false)
public class OrderListResource extends ResourceSupport {

    private final List<OrderResource> orders;


    public OrderListResource(List<OrderResource> orders, int pageCurrent, long pageLast, int pageSize, boolean isSelf, Map<String, String> params) {
        this.orders = orders;
        add(linkTo(OrderController.class).withRel(LinkConstant.CREATE_REL).withType(LinkConstant.POST_METHOD));
        if (isSelf) {
            add(Paginator.buildPaginationLinks(EndPointConstant.ORDER_ENDPOINT + "/self", pageCurrent, pageLast, pageSize, params));

        } else {
            add(Paginator.buildPaginationLinks(EndPointConstant.ORDER_ENDPOINT, pageCurrent, pageLast, pageSize, params));
        }
    }


}
