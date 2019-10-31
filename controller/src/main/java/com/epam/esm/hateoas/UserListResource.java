package com.epam.esm.hateoas;

import com.epam.esm.controller.UserController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class UserListResource extends ResourceSupport {

    private List<UserResource> users;

    public UserListResource(List<UserResource> users) {
        this.users = users;
        add(linkTo(UserController.class).withSelfRel().withType(LinkConstant.GET_METHOD));

    }
}
