package com.epam.esm.hateoas;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.GiftCertificateDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.constant.LinkConstant.DELETE_METHOD;
import static com.epam.esm.constant.LinkConstant.GET_METHOD;
import static com.epam.esm.constant.LinkConstant.PATCH_METHOD;
import static com.epam.esm.constant.LinkConstant.PUT_METHOD;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
@EqualsAndHashCode(callSuper = false)
public class CertificateResource extends ResourceSupport {

    private final GiftCertificateDTO giftCertificate;
    private final List<TagResource> tags;

    public CertificateResource(GiftCertificateDTO giftCertificate, List<TagResource> tags) {
        this.giftCertificate = giftCertificate;
        this.tags = tags;
        add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(GET_METHOD));
        if (getAuthorities().contains(RoleConstant.ROLE_ADMIN)) {
            add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(DELETE_METHOD));
            add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(PUT_METHOD));
            add(linkTo(CertificateController.class).slash(giftCertificate.getId()).withSelfRel().withType(PATCH_METHOD));
        }
    }

    private List<String> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
