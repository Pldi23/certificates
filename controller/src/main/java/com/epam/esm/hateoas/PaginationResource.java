package com.epam.esm.hateoas;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

@Getter
@EqualsAndHashCode(callSuper = false)
@Builder
public class PaginationResource extends ResourceSupport {

    private final boolean isCurrent;
    private final String page;
    private final Link link;

    public PaginationResource(boolean isCurrent, String page, Link link) {
        this.isCurrent = isCurrent;
        this.page = page;
        this.link = link;
    }
}
