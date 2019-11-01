package com.epam.esm.hateoas;

import com.epam.esm.constant.LinkConstant;
import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.OrderDTO;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static com.epam.esm.constant.LinkConstant.*;


@Getter
public class OrderResource extends ResourceSupport {

    private final OrderDTO order;

    public OrderResource(OrderDTO order) {
        this.order = order;
        add(linkTo(OrderController.class).slash(order.getId()).withSelfRel().withType(GET_METHOD));
        add(linkTo(OrderController.class).slash(order.getId()).withSelfRel().withType(DELETE_METHOD));
        add(linkTo(OrderController.class).slash(order.getId()).withSelfRel().withType(PUT_METHOD));
        add(linkTo(OrderController.class).slash(order.getId()).slash(CERTIFICATES).withSelfRel().withType(GET_METHOD));
        add(linkTo(UserController.class).slash(order.getUserId()).withSelfRel().withType(GET_METHOD));

//        add(linkTo(OrderController.class).withSelfRel().withType(POST_METHOD));

//        add(order.getGiftCertificates().stream().map(giftCertificateDTO -> linkTo(CertificateController.class).slash(giftCertificateDTO.getId())
//                .withRel(LinkConstant.CERTIFICATE_LINK_REL).withType(GET_METHOD)).collect(Collectors.toSet()));
//        Set<Link> tagLinks = new HashSet<>();
//        order.getGiftCertificates().forEach(giftCertificateDTO -> giftCertificateDTO.getTags().forEach(tag -> tagLinks.add(linkTo(TagController.class).slash(tag.getId()).withRel("tags").withType(GET_METHOD))));
//        add(tagLinks);

//        add(linkTo(UserController.class).slash(order.getUserId()).withRel(LinkConstant.USER_LINK_REL).withType(GET_METHOD));
    }
}
