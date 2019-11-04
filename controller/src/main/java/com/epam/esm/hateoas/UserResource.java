package com.epam.esm.hateoas;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static com.epam.esm.constant.LinkConstant.DELETE_METHOD;
import static com.epam.esm.constant.LinkConstant.DELETE_REL;
import static com.epam.esm.constant.LinkConstant.GET_METHOD;
import static com.epam.esm.constant.LinkConstant.PATCH_METHOD;
import static com.epam.esm.constant.LinkConstant.PATCH_REL;
import static com.epam.esm.constant.LinkConstant.PUT_METHOD;
import static com.epam.esm.constant.LinkConstant.PUT_REL;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
@EqualsAndHashCode(callSuper = false)
public class UserResource extends ResourceSupport {

    private UserDTO user;

    public UserResource(UserDTO user) {
        this.user = user;
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(GET_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withRel(PUT_REL).withType(PUT_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withRel(DELETE_REL).withType(DELETE_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withRel(PATCH_REL).withType(PATCH_METHOD));
    }


}
