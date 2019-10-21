package com.epam.esm.controller;

import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.security.SecurityConstants;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Translator;
import com.epam.esm.validator.TagCostSortValid;
import com.epam.esm.validator.OrderSortValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.UserSortValid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-11.
 * @version 0.0.1
 */

@RestController
@RequestMapping("/users")
@Validated
@ExposesResourceFor(UserDTO.class)
public class UserController {

    private UserService userService;
    private OrderService orderService;
    private LinkCreator linkCreator;
    private DtoParser dtoParser;
    private TagService tagService;
    private TokenCreator tokenCreator;

    public UserController(UserService userService, OrderService orderService, LinkCreator linkCreator,
                          DtoParser dtoParser, TagService tagService, TokenCreator tokenCreator) {
        this.userService = userService;
        this.orderService = orderService;
        this.linkCreator = linkCreator;
        this.dtoParser = dtoParser;
        this.tagService = tagService;
        this.tokenCreator = tokenCreator;
    }


    @PostMapping
    public ResponseEntity save(@RequestBody @Valid UserDTO userDTO) {
        UserDTO saved = userService.save(userDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityConstants.TOKEN_HEADER, tokenCreator.createJwt(saved.getEmail(), List.of(saved.getRole())));
        return new ResponseEntity<>(linkCreator.toResource(saved), httpHeaders, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity getAll(@RequestParam @PageAndSizeValid(message = "{violation.page.size}")
                                     @UserSortValid(message = "{violation.user.sort}") Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        List<UserDTO> users = userService.findAll(pageAndSortDTO);
        return ResponseEntity.ok(!users.isEmpty() ? users.stream().map(userDTO -> linkCreator.toResource(userDTO)) :
                ResponseEntity.notFound().build());
    }

    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        return ResponseEntity.ok(linkCreator.toResource(userService.findOne(id)));
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
        return ResponseEntity.ok(orderService.findByCriteria(orderSearchCriteriaDTO, pageAndSortDTO));
    }

    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        userService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody @Valid UserDTO userDTO,
                                 @PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        try {
            return ResponseEntity.ok(linkCreator.toResource(userService.update(userDTO, id)));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.user.exist"), userDTO.getEmail()));
        }
    }

    @PreAuthorize("@securityChecker.check(#id) or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity patch(@RequestBody @Valid UserPatchDTO userPatchDTO,
                                @PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        try {
            return ResponseEntity.ok(linkCreator.toResource(userService.patch(userPatchDTO, id)));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.user.exist"), userPatchDTO.getEmail()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByUser(
            @PathVariable @Min(value = 0, message = "{violation.id}") Long id,
            @PageAndSizeValid(message = "{violation.page.size}")
            @TagCostSortValid(message = "{violation.tag.sort.cost}")
            @RequestParam Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        return ResponseEntity.ok(tagService.findTagsByUser(id, pageAndSortDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/tags/popular")
    public ResponseEntity getMostPopularTagsByUser(
            @PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        return ResponseEntity.ok(tagService.findMostCostEffectiveTag(id));
    }


}
