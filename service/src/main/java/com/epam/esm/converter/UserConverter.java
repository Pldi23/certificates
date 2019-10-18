package com.epam.esm.converter;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.UserRoleException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
@Component
public class UserConverter {

    public User convert(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
//                .role(Role.builder()
//                        .id(userDTO.getRole().getId())
//                        .value(userDTO.getRole().getValue())
//                        .build())
                .role(convertRole(userDTO.getRole()))
                .build();
    }

    public UserDTO convert(User user) {
        List<Long> ordersIds = user.getOrders() != null ? user.getOrders().stream()
                .map(Order::getId)
                .collect(Collectors.toList()) : new ArrayList<>();
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().getValue())
                .ordersIds(ordersIds)
//                .role(Role.builder()
//                        .id(user.getRole().getId())
//                        .value(user.getRole().getValue())
//                        .build())
                .build();
    }

    public Role convertRole(String role) {
        Role convertedRole;
        switch (role) {
            case "ROLE_ADMIN":
                convertedRole = Role.builder().id(1L).value("ROLE_ADMIN").build();
                break;
            case "ROLE_USER":
                convertedRole = Role.builder().id(2L).value("ROLE_USER").build();
                break;
            case "ROLE_GUEST":
                convertedRole = Role.builder().id(3L).value("ROLE_GUEST").build();
                break;
            default:
                throw new UserRoleException("unexpected role");
        }
        return convertedRole;
    }
}
