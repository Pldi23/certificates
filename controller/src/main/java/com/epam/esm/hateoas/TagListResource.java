package com.epam.esm.hateoas;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.controller.TagController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static com.epam.esm.constant.LinkConstant.POST_METHOD;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Getter
public class TagListResource extends ResourceSupport {

    private final List<TagResource> tagList;

    public TagListResource(List<TagResource> tagList, int pageCurrent, long pageLast, int pageSize) {
        this.tagList = tagList;
        add(linkTo(TagController.class).withSelfRel().withType(POST_METHOD));

        add(Paginator.buildPaginationLinks(EndPointConstant.TAG_ENDPOINT, pageCurrent, pageLast, pageSize));
    }

}
