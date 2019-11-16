package com.epam.esm.hateoas;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.LinkConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.controller.CertificateController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
@EqualsAndHashCode(callSuper = false)
public class CertificateListResource extends ResourceSupport {

    private List<CertificateResource> certificates;

    public CertificateListResource(List<CertificateResource> certificates, int pageCurrent, long pageLast, int pageSize, Map<String, String> params) {
        this.certificates = certificates;
        if (getAuthorities().contains(RoleConstant.ROLE_ADMIN)) {
            add(linkTo(CertificateController.class).withRel(LinkConstant.CREATE_REL).withType(LinkConstant.POST_METHOD));
        }
        add(Paginator.buildPaginationLinks(EndPointConstant.CERTIFICATE_ENDPOINT, pageCurrent, pageLast, pageSize, params));
    }

    private List<String> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
