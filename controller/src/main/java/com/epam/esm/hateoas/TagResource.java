package com.epam.esm.hateoas;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDTO;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.hateoas.LinkConstant.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class TagResource extends ResourceSupport {

    private final TagDTO tag;

    public TagResource(TagDTO tag) {
        this.tag = tag;
        add(linkTo(TagController.class).slash(tag.getId()).withSelfRel().withType(GET_METHOD));
        add(linkTo(TagController.class).slash(tag.getId()).slash(TAG_STATS).withRel(TAG_STATS).withType(GET_METHOD));
        if (getAuthorities().contains(RoleConstant.ROLE_ADMIN)) {
            add(linkTo(TagController.class).slash(tag.getId()).withSelfRel().withType(DELETE_METHOD));
            add(linkTo(TagController.class).withSelfRel().withType(POST_METHOD));
        }
    }

    private List<String> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
