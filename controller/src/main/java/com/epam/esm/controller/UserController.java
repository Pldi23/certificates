package com.epam.esm.controller;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.constant.SecurityConstant;
import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.hateoas.OrderListResource;
import com.epam.esm.hateoas.OrderResource;
import com.epam.esm.hateoas.TagDetailsListResource;
import com.epam.esm.hateoas.TagDetailsResource;
import com.epam.esm.hateoas.TagListResource;
import com.epam.esm.hateoas.TagResource;
import com.epam.esm.hateoas.UserListResource;
import com.epam.esm.hateoas.UserResource;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.AppUserDetailsService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Translator;
import com.epam.esm.validator.OrderSortValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.TagSortValid;
import com.epam.esm.validator.UserSortValid;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * users resource endpoint
 *
 * @author Dzmitry Platonov on 2019-10-11.
 * @version 0.0.1
 */

@RestController
@RequestMapping(EndPointConstant.USER_ENDPOINT)
@Validated
@ExposesResourceFor(UserDTO.class)
@Log4j2
public class UserController {

    private static final String USER_EXIST_MESSAGE = "exception.user.exist";

    private UserService userService;
    private OrderService orderService;
    private DtoParser dtoParser;
    private TagService tagService;
    private TokenCreator tokenCreator;
    private AppUserDetailsService detailsService;

    public UserController(UserService userService, OrderService orderService,
                          DtoParser dtoParser, TagService tagService, TokenCreator tokenCreator,
                          AppUserDetailsService detailsService) {
        this.userService = userService;
        this.orderService = orderService;
        this.dtoParser = dtoParser;
        this.tagService = tagService;
        this.tokenCreator = tokenCreator;
        this.detailsService = detailsService;
    }

    @PreAuthorize("@securityChecker.checkRegisterRole(#userDTO)")
    @PostMapping
    public ResponseEntity save(@RequestBody @Valid UserDTO userDTO) {
        UserDTO saved;
        try {
            saved = userService.save(userDTO);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE),
                    userDTO.getEmail()));
        }
        AppUserPrinciple principle = (AppUserPrinciple) detailsService.loadUserByUsername(saved.getEmail());
        List<String> roles = principle.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String token = tokenCreator.createJwt(principle.getUsername(), roles);
        String refreshToken = tokenCreator.createRefreshToken(principle.getUsername(), roles);
        detailsService.update(principle, refreshToken);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityConstant.TOKEN_HEADER, tokenCreator.createJwt(saved.getEmail(), List.of(saved.getRole())));
        httpHeaders.add(SecurityConstant.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
        httpHeaders.add(SecurityConstant.REFRESH_TOKEN_HEADER, refreshToken);
        return new ResponseEntity<>(new UserResource(saved), httpHeaders, HttpStatus.OK);
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity getAll(@RequestParam
                                 @PageAndSizeValid(message = "{violation.page.size}")
                                 @UserSortValid(message = "{violation.user.sort}") Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        List<UserDTO> users = userService.findAll(pageAndSortDTO);
        return ResponseEntity.ok(!users.isEmpty() ? new UserListResource(users.stream().map(UserResource::new).collect(Collectors.toList())) :
                ResponseEntity.notFound().build());
    }

    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        return ResponseEntity.ok(new UserResource(userService.findOne(id)));
    }

    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/orders")
    public ResponseEntity findUserOrders(
            @PathVariable("id")
            @Min(value = 0, message = "{violation.id}") Long id,
            @PageAndSizeValid(message = "{violation.page.size}")
            @OrderSortValid(message = "{violation.order.sort}") @RequestParam Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        OrderSearchCriteriaDTO orderSearchCriteriaDTO = OrderSearchCriteriaDTO.builder()
                .userId(id)
                .build();
        return ResponseEntity.ok(new OrderListResource(orderService.findByCriteria(orderSearchCriteriaDTO, pageAndSortDTO).stream()
                .map(OrderResource::new).collect(Collectors.toList()),
                pageAndSortDTO.getPage(),
                orderService.lastPageNumber(orderSearchCriteriaDTO, pageAndSortDTO),
                pageAndSortDTO.getSize()));
    }

    @PreAuthorize("@securityChecker.check(#id) or @securityChecker.checkUser(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        userService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PreAuthorize(value = "(@securityChecker.check(#id) or @securityChecker.checkUser(#id)) and @securityChecker.checkUpdateRole(#userDTO)")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody @Valid UserDTO userDTO,
                                 @PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        try {
            return ResponseEntity.ok(new UserResource(userService.update(userDTO, id)));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE),
                    userDTO.getEmail()));
        }
    }

    @PreAuthorize("(@securityChecker.check(#id) or @securityChecker.checkUser(#id)) and @securityChecker.checkRegisterRole(#userPatchDTO)")
    @PatchMapping("/{id}")
    public ResponseEntity patch(@RequestBody @Valid UserPatchDTO userPatchDTO,
                                @PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        try {
            return ResponseEntity.ok(new UserResource(userService.patch(userPatchDTO, id)));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE),
                    userPatchDTO.getEmail()));
        }
    }

    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByUser(
            @PathVariable @Min(value = 0, message = "{violation.id}") Long id,
            @PageAndSizeValid(message = "{violation.page.size}")
            @TagSortValid(message = "{violation.tag.sort.cost}")
            @RequestParam Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        return ResponseEntity.ok(new TagListResource(tagService.findTagsByUser(id, pageAndSortDTO).stream()
                .map(TagResource::new).collect(Collectors.toList())));
    }


    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/tags/popular")
    public ResponseEntity getMostPopularTagsByUserWithDetails(
            @PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        return ResponseEntity.ok(new TagDetailsListResource(tagService.findMostCostEffectiveTagWithStats(id).stream()
                .map(TagDetailsResource::new).collect(Collectors.toList())));
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @PreAuthorize("@securityChecker.checkPatchRole(#userPatchDTO)")
    @PatchMapping("/self")
    public ResponseEntity patchSelf(@RequestBody @Valid UserPatchDTO userPatchDTO) {
        AppUserPrinciple principle = (AppUserPrinciple) detailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            return ResponseEntity.ok(new UserResource(userService.patch(userPatchDTO, principle.getUser().getId())));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE),
                    userPatchDTO.getEmail()));
        }
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @PreAuthorize("@securityChecker.checkUpdateRole(#userDTO)")
    @PutMapping("/self")
    public ResponseEntity putSelf(@RequestBody @Valid UserDTO userDTO) {
        AppUserPrinciple principle = (AppUserPrinciple) detailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            return ResponseEntity.ok(new UserResource(userService.update(userDTO, principle.getUser().getId())));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE),
                    userDTO.getEmail()));
        }
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @DeleteMapping("/self")
    public ResponseEntity deleteSelf() {
        AppUserPrinciple principle = (AppUserPrinciple) detailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        userService.delete(principle.getUser().getId());
        return ResponseEntity.status(204).build();
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping("/self")
    public ResponseEntity getSelf() {
        AppUserPrinciple principle = (AppUserPrinciple) detailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UserDTO userDTO = userService.findOne(principle.getUser().getId());

        return ResponseEntity.ok(new UserResource(userDTO));
    }

}
