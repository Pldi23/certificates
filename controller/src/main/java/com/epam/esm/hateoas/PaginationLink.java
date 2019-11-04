package com.epam.esm.hateoas;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.XmlAttribute;


@Getter
@EqualsAndHashCode(callSuper = false)
class PaginationLink extends Link {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private boolean isCurrent;

    PaginationLink(Link link, String name, boolean isCurrent) {
        super(link.getHref(), link.getRel());
        this.name = name;
        this.isCurrent = isCurrent;
    }
}
