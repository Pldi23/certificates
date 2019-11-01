package com.epam.esm.hateoas;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static com.epam.esm.constant.LinkConstant.DELETE_METHOD;
import static com.epam.esm.constant.LinkConstant.GET_METHOD;
import static com.epam.esm.constant.LinkConstant.PATCH_METHOD;
import static com.epam.esm.constant.LinkConstant.PUT_METHOD;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
@EqualsAndHashCode(callSuper = false)
public class UserResource extends ResourceSupport {

    private UserDTO user;

    public UserResource(UserDTO user) {
        this.user = user;
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(GET_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(PUT_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(DELETE_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(PATCH_METHOD));
    }


}
