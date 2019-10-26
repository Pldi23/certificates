package com.epam.esm.hateoas;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.AppUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * hateoas links-creator helper class
 *
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
@Component
@Log4j2
public class LinkCreator {

    private static final String GET_METHOD = "GET";
    private static final String PUT_METHOD = "PUT";
    private static final String PATCH_METHOD = "PATCH";
    private static final String DELETE_METHOD = "DELETE";
    private static final String MEDIA_APPLICATION_JSON = "application/json";

    private AppUserDetailsService userDetailsService;

    public LinkCreator(AppUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Resource<OrderDTO> toResource(OrderDTO orderDTO) {
        AppUserPrinciple principle = getPrinciple();
        List<Link> links = new ArrayList<>();
        links.add(linkTo(OrderController.class).slash(orderDTO.getId()).withSelfRel().withMedia(MEDIA_APPLICATION_JSON).withType(GET_METHOD));
        if (principle != null && (principleCheck(principle, RoleConstant.ROLE_ADMIN) || orderDTO.getUserId().equals(principle.getUser().getId()))) {
            links.add(linkTo(OrderController.class).slash(orderDTO.getId()).withSelfRel().withType(PUT_METHOD));
            links.add(linkTo(OrderController.class).slash(orderDTO.getId()).withSelfRel().withType(DELETE_METHOD));
        }

        orderDTO.getGiftCertificates()
                .forEach(i -> {
                    if (!uniqueLinkCheck(links, linkTo(CertificateController.class)
                            .slash(i.getId()).withRel(LinkConstant.CERTIFICATE_LINK_REL))) {
                        links.add(linkTo(CertificateController.class).slash(i.getId())
                                .withRel(LinkConstant.CERTIFICATE_LINK_REL).withType(GET_METHOD));
                        if (principleCheck(principle, RoleConstant.ROLE_ADMIN)) {
                            links.add(linkTo(CertificateController.class).slash(i.getId())
                                    .withRel(LinkConstant.CERTIFICATE_LINK_REL).withType(PUT_METHOD));
                            links.add(linkTo(CertificateController.class).slash(i.getId())
                                    .withRel(LinkConstant.CERTIFICATE_LINK_REL).withType(DELETE_METHOD));
                            links.add(linkTo(CertificateController.class).slash(i.getId())
                                    .withRel(LinkConstant.CERTIFICATE_LINK_REL).withType(PATCH_METHOD));
                        }
                    }
                });


        links.add(linkTo(UserController.class).slash(orderDTO.getUserId()).withRel(LinkConstant.USER_LINK_REL).withType(GET_METHOD));
        if (principle != null && (principleCheck(principle, RoleConstant.ROLE_ADMIN) || orderDTO.getUserId().equals(principle.getUser().getId()))) {
            links.add(linkTo(UserController.class).slash(orderDTO.getUserId()).withRel(LinkConstant.USER_LINK_REL).withType(PUT_METHOD));
            links.add(linkTo(UserController.class).slash(orderDTO.getUserId()).withRel(LinkConstant.USER_LINK_REL).withType(PATCH_METHOD));
            links.add(linkTo(UserController.class).slash(orderDTO.getUserId()).withRel(LinkConstant.USER_LINK_REL).withType(DELETE_METHOD));

        }
        links.forEach(link -> link.withMedia(MEDIA_APPLICATION_JSON));
        return new Resource<>(orderDTO, links);
    }

    public Resource<TagDTO> toResource(TagDTO tagDTO) {
        return new Resource<>(tagDTO,
                linkTo(TagController.class).slash(tagDTO.getId()).withSelfRel());
    }

    public Resource<GiftCertificateDTO> toResource(GiftCertificateDTO giftCertificateDTO) {
        AppUserPrinciple principle = getPrinciple();
        List<Link> links = new ArrayList<>();
        links.add(linkTo(CertificateController.class).slash(giftCertificateDTO.getId()).withSelfRel().withType(GET_METHOD));
        if (principleCheck(principle, RoleConstant.ROLE_ADMIN)) {
            links.add(linkTo(CertificateController.class).slash(giftCertificateDTO.getId()).withSelfRel().withType(PUT_METHOD));
            links.add(linkTo(CertificateController.class).slash(giftCertificateDTO.getId()).withSelfRel().withType(DELETE_METHOD));
        }
        if (principle != null) {
            giftCertificateDTO.getTags().forEach(tagDTO -> {
                links.add(linkTo(TagController.class).slash(tagDTO.getId()).withRel(LinkConstant.TAG_LINK_REL).withType(GET_METHOD));
                if (principleCheck(principle, RoleConstant.ROLE_ADMIN)) {
                    links.add(linkTo(TagController.class).slash(tagDTO.getId()).withRel(LinkConstant.TAG_LINK_REL).withType(PUT_METHOD));
                    links.add(linkTo(TagController.class).slash(tagDTO.getId()).withRel(LinkConstant.TAG_LINK_REL).withType(DELETE_METHOD));
                }
            });
        }
        return new Resource<>(giftCertificateDTO, links);
    }

    public Resource<UserDTO> toResource(UserDTO userDTO) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(UserController.class).slash(userDTO.getId()).withSelfRel());
        userDTO.getOrdersIds().forEach(i -> links.add(linkTo(OrderController.class).slash(i).withRel(LinkConstant.ORDER_LINK_REL)));
        return new Resource<>(userDTO, links);
    }

    private AppUserPrinciple getPrinciple() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).noneMatch(s-> s.equals(RoleConstant.ROLE_ANONYMOUS)) ?
        (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) authentication.getPrincipal()) : null;
    }

    private boolean principleCheck(AppUserPrinciple principle, String role) {
        return principle != null && principle.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(s -> s.equals(role));
    }

    private boolean uniqueLinkCheck(List<Link> links, Link link) {
        return links.contains(link.withType(GET_METHOD));
    }
}
