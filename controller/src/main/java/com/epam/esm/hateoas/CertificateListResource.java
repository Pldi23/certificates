package com.epam.esm.hateoas;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.LinkConstant;
import com.epam.esm.controller.CertificateController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
@EqualsAndHashCode(callSuper = false)
public class CertificateListResource extends ResourceSupport {

    private List<CertificateResource> certificates;

    public CertificateListResource(List<CertificateResource> certificates, int pageCurrent, long pageLast, int pageSize) {
        this.certificates = certificates;
        add(linkTo(CertificateController.class).withSelfRel().withType(LinkConstant.POST_METHOD));

        add(Paginator.buildPaginationLinks(EndPointConstant.CERTIFICATE_ENDPOINT, pageCurrent, pageLast, pageSize));


    }
}
