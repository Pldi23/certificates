package com.epam.esm.hateoas;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.GiftCertificateDTO;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.hateoas.LinkConstant.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class CertificateResource extends ResourceSupport {

    private final GiftCertificateDTO giftCertificate;

    public CertificateResource(GiftCertificateDTO giftCertificate) {
        this.giftCertificate = giftCertificate;
        add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(GET_METHOD));
        if (getAuthorities().contains(RoleConstant.ROLE_ADMIN)) {
            add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(DELETE_METHOD));
            add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(PUT_METHOD));
            add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(PATCH_METHOD));
            add(linkTo(CertificateController.class).withSelfRel().withType(POST_METHOD));
        }
        add(giftCertificate.getTags().stream().map(tagDTO -> linkTo(TagController.class).slash(tagDTO.getId())
                .withRel(TAG_LINK_REL).withType(GET_METHOD)).collect(Collectors.toSet()));
    }

    private List<String> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
