package com.epam.esm.hateoas;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.LinkConstant;
import com.epam.esm.controller.OrderController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Getter
@EqualsAndHashCode(callSuper = false)
public class OrderListResource extends ResourceSupport {

    private final List<OrderResource> orders;



    public OrderListResource(List<OrderResource> orders, int pageCurrent, long pageLast, int pageSize) {
        this.orders = orders;
        add(linkTo(OrderController.class).withRel(LinkConstant.CREATE_REL).withType(LinkConstant.POST_METHOD));
        add(Paginator.buildPaginationLinks(EndPointConstant.ORDER_ENDPOINT, pageCurrent, pageLast, pageSize));
    }


}
