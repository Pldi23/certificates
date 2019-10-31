package com.epam.esm.hateoas;

import com.epam.esm.controller.CertificateController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class CertificateListResource extends ResourceSupport {

    private List<CertificateResource> certificates;

    public CertificateListResource(List<CertificateResource> certificates) {
        this.certificates = certificates;
        add(linkTo(CertificateController.class).withSelfRel().withType(LinkConstant.GET_METHOD));
    }
}
