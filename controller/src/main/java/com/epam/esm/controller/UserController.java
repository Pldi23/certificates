package com.epam.esm.controller;

import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.TagCostSortValid;
import com.epam.esm.validator.OrderSortValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.TagSortValid;
import com.epam.esm.validator.UserSortValid;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
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

    public UserController(UserService userService, OrderService orderService, LinkCreator linkCreator, DtoParser dtoParser, TagService tagService) {
        this.userService = userService;
        this.orderService = orderService;
        this.linkCreator = linkCreator;
        this.dtoParser = dtoParser;
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(linkCreator.toResource(userService.save(userDTO)));
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam @PageAndSizeValid @UserSortValid Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        List<UserDTO> users = userService.findAll(pageAndSortDTO);
        return ResponseEntity.ok(!users.isEmpty() ? users.stream().map(userDTO -> linkCreator.toResource(userDTO)) :
                ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable @Min(0) Long id) {
        return ResponseEntity.ok(linkCreator.toResource(userService.findOne(id)));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity findUserOrders(
            @PathVariable("id") @Min(0) Long id,
            @PageAndSizeValid @OrderSortValid @RequestParam Map<String,String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        OrderSearchCriteriaDTO orderSearchCriteriaDTO = OrderSearchCriteriaDTO.builder()
                .userId(id)
                .build();
        return ResponseEntity.ok(orderService.findByCriteria(orderSearchCriteriaDTO, pageAndSortDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable @Min(0) Long id) {
        userService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody @Valid UserDTO userDTO, @PathVariable @Min(0) Long id) {
        return ResponseEntity.ok(linkCreator.toResource(userService.update(userDTO, id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity patch(@RequestBody @Valid UserPatchDTO userPatchDTO, @PathVariable @Min(0) Long id) {
        return ResponseEntity.ok(linkCreator.toResource(userService.patch(userPatchDTO, id)));
    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByUser(
            @PathVariable @Min(0) Long id,
            @PageAndSizeValid @TagCostSortValid @RequestParam Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        return ResponseEntity.ok(tagService.findTagsByUser(id, pageAndSortDTO));
    }
}
