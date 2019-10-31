package com.epam.esm.hateoas;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDetailsDTO;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class TagDetailsResource extends ResourceSupport {

    private final TagDetailsDTO tagDetails;

    public TagDetailsResource(TagDetailsDTO tagDetails) {
        this.tagDetails = tagDetails;
        add(linkTo(TagController.class).slash(tagDetails.getTag().getId()).withSelfRel().withType(LinkConstant.GET_METHOD));
    }
}
