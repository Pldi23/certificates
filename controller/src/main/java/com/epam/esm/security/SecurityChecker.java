package com.epam.esm.security;


import com.epam.esm.constant.RequestConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.AppUserDetailsService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * to check principal access
 *
 * @author Dzmitry Platonov on 2019-10-19.
 * @version 0.0.1
 */
@Component
@Log4j2
public class SecurityChecker {

    private AppUserDetailsService userDetailsService;
    private OrderService orderService;
    private UserService userService;


    public SecurityChecker(AppUserDetailsService userDetailsService, OrderService orderService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.orderService = orderService;
        this.userService = userService;
    }

    public boolean check(Long id) {
        AppUserPrinciple principle = (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return id.equals(principle.getUser().getId());
    }

    public boolean checkUser(Long id) {
        AppUserPrinciple principle = (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<String> roles = principle.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        UserDTO userDTO = userService.findOne(id);
        return !roles.contains(RoleConstant.ROLE_USER) && !userDTO.getRole().equals(RoleConstant.ROLE_ADMIN);
    }

    public boolean checkOrder(ResponseEntity responseEntity) {
        AppUserPrinciple principle = (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (principle != null && responseEntity != null && responseEntity.getBody() != null) {
            Resource resource = (Resource) responseEntity.getBody();
            OrderDTO orderDTO = (OrderDTO) resource.getContent();
            return orderDTO.getUserId().equals(principle.getUser().getId());
        } else {
            return false;
        }
    }

    public boolean check(Map<String, String> params) {
        AppUserPrinciple principle = (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            if (params.containsKey(RequestConstant.USER_ID) && principle.getUser().getId() != Long.parseLong(params.get(RequestConstant.USER_ID))) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return !params.containsKey(RequestConstant.EMAIL) || principle.getUsername().equals(params.get(RequestConstant.EMAIL));
    }

    public boolean checkOrderAuthorities(Long id) {
        AppUserPrinciple principle = (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        OrderDTO orderDTO = orderService.findOne(id);
        if (orderDTO != null && orderDTO.getUserId() != null && principle != null) {
            return orderDTO.getUserId().equals(principle.getUser().getId());
        } else {
            return false;
        }
    }
}
