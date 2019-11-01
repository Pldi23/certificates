package com.epam.esm.hateoas;

import com.epam.esm.constant.EndPointConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;


@Getter
@EqualsAndHashCode(callSuper = false)
public class UserListResource extends ResourceSupport {

    private List<UserResource> users;

    public UserListResource(List<UserResource> users, int pageCurrent, long pageLast, int pageSize) {
        this.users = users;
        add(Paginator.buildPaginationLinks(EndPointConstant.USER_ENDPOINT, pageCurrent, pageLast, pageSize));
    }
}
