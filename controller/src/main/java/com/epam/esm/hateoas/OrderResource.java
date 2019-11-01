package com.epam.esm.hateoas;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static com.epam.esm.constant.LinkConstant.DELETE_METHOD;
import static com.epam.esm.constant.LinkConstant.GET_METHOD;
import static com.epam.esm.constant.LinkConstant.PUT_METHOD;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Getter
@EqualsAndHashCode(callSuper = false)
public class OrderResource extends ResourceSupport {

    private final OrderDTO order;
    private final List<CertificateResource> certificates;

    public OrderResource(OrderDTO order, List<CertificateResource> certificates) {
        this.order = order;
        this.certificates = certificates;
        add(linkTo(OrderController.class).slash(order.getId()).withSelfRel().withType(GET_METHOD));
        add(linkTo(OrderController.class).slash(order.getId()).withSelfRel().withType(DELETE_METHOD));
        add(linkTo(OrderController.class).slash(order.getId()).withSelfRel().withType(PUT_METHOD));
    }
}
