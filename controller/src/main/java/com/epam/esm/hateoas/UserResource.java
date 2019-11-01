package com.epam.esm.hateoas;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDTO;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.constant.LinkConstant.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
public class UserResource extends ResourceSupport {

    private UserDTO user;

    public UserResource(UserDTO user) {
        this.user = user;
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(GET_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(PUT_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(DELETE_METHOD));
        add(linkTo(UserController.class).slash(user.getId()).withSelfRel().withType(PATCH_METHOD));

//        List<Link> ordersLink = new ArrayList<>();
//        ordersLink.add(linkTo(OrderController.class).withRel(ORDER_LINK_REL).withType(POST_METHOD));
//        user.getOrdersIds().forEach(i -> {
//            ordersLink.add(linkTo(OrderController.class).slash(i).withRel(ORDER_LINK_REL).withType(GET_METHOD));
//            ordersLink.add(linkTo(OrderController.class).slash(i).withRel(ORDER_LINK_REL).withType(PUT_METHOD));
//            ordersLink.add(linkTo(OrderController.class).slash(i).withRel(ORDER_LINK_REL).withType(DELETE_METHOD));
//        });
//        add(ordersLink);

        add(linkTo(UserController.class).slash(user.getId()).slash("orders").withRel("user orders").withType(GET_METHOD));
        if (getAuthorities().contains(RoleConstant.ROLE_ADMIN)) {
//            add(linkTo(UserController.class).slash(user.getId()).slash("tags").withRel("user tags").withType(GET_METHOD));
            add(linkTo(UserController.class).slash(user.getId()).slash("tags").slash("popular").withRel("popular tag").withType(GET_METHOD));
        }
    }

    private List<String> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
