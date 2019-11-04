package com.epam.esm.hateoas;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.constant.LinkConstant.DELETE_METHOD;
import static com.epam.esm.constant.LinkConstant.DELETE_REL;
import static com.epam.esm.constant.LinkConstant.GET_METHOD;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
@EqualsAndHashCode(callSuper = false)
public class TagResource extends ResourceSupport {

    private final TagDTO tag;

    public TagResource(TagDTO tag) {
        this.tag = tag;
        if (getAuthorities().contains(RoleConstant.ROLE_ADMIN) || getAuthorities().contains(RoleConstant.ROLE_USER)) {
            add(linkTo(TagController.class).slash(tag.getId()).withSelfRel().withType(GET_METHOD));
        }
        if (getAuthorities().contains(RoleConstant.ROLE_ADMIN)) {
            add(linkTo(TagController.class).slash(tag.getId()).withRel(DELETE_REL).withType(DELETE_METHOD));
        }
    }

    private List<String> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
