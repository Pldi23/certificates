package com.epam.esm.hateoas;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
public class TagDetailsListResource extends ResourceSupport {

    private final List<TagDetailsResource> tags;

    public TagDetailsListResource(List<TagDetailsResource> tags) {
        this.tags = tags;
    }
}
