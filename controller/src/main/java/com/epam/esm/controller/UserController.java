package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.service.UserService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

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
    private LinkCreator linkCreator;

    public UserController(UserService userService, LinkCreator linkCreator) {
        this.userService = userService;
        this.linkCreator = linkCreator;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(linkCreator.toResource(userService.save(userDTO)));
    }

    @GetMapping
    public ResponseEntity getAll() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(!users.isEmpty() ? users.stream().map(userDTO -> linkCreator.toResource(userDTO)) :
                ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable @Min(0) Long id) {
        return ResponseEntity.ok(linkCreator.toResource(userService.findOne(id)));
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
}
