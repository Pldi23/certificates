package com.epam.esm.hateoas;

import com.epam.esm.constant.LinkConstant;
import com.epam.esm.controller.TagController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class TagDetailsListResource extends ResourceSupport {

    private final List<TagDetailsResource> tags;

    public TagDetailsListResource(List<TagDetailsResource> tags) {
        this.tags = tags;
        add(linkTo(TagController.class).withSelfRel().withType(LinkConstant.GET_METHOD));
    }
}
