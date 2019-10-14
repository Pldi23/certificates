package com.epam.esm.hateoas;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
@Component
public class LinkCreator {

    public Resource<OrderDTO> toResource(OrderDTO orderDTO) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(OrderController.class).slash(orderDTO.getId()).withSelfRel());
        orderDTO.getCertificatesIds()
                .forEach(i -> links.add(linkTo(CertificateController.class).slash(i).withRel("gift certificates")));
        return new Resource<>(orderDTO, links);
    }

    public Resource<TagDTO> toResource(TagDTO tagDTO) {
        return new Resource<>(tagDTO,
                linkTo(TagController.class).slash(tagDTO.getId()).withSelfRel());
    }

    public Resource<GiftCertificateDTO> toResource(GiftCertificateDTO giftCertificateDTO) {
        List<Link> links = new ArrayList<>(giftCertificateDTO.getTags().size() + 1);
        links.add(linkTo(CertificateController.class).slash(giftCertificateDTO.getId()).withSelfRel());
        giftCertificateDTO.getTags().forEach(tagDTO -> links.add(linkTo(TagController.class).slash(tagDTO.getId()).withRel("tag")));
        return new Resource<>(giftCertificateDTO, links);
    }

    public Resource<UserDTO> toResource(UserDTO userDTO) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(UserController.class).slash(userDTO.getId()).withSelfRel());
        userDTO.getOrdersIds().forEach(i -> links.add(linkTo(OrderController.class).slash(i).withRel("orders")));
        return new Resource<>(userDTO, links);
    }
}
