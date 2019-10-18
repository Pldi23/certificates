package com.epam.esm.security;

import com.epam.esm.dto.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-17.
 * @version 0.0.1
 */
@RestController
public class SecurityController {


    @GetMapping(value = "/bearer")
    public ResponseEntity getPublicMessage() {
        return ResponseEntity.ok(new MessageDTO("Hello, you could take your bearer from header", 200));
    }


    @GetMapping(value = "/private")
    public ResponseEntity getPrivateMessage() {
        return ResponseEntity.ok(new MessageDTO("private", 200));
    }

//    @RolesAllowed("user")
    @Secured({"ROLE_USER"})
    @GetMapping(value = "/user")
    public ResponseEntity getUserMessage() {
        return ResponseEntity.ok(new MessageDTO("for user", 200));
    }

//    @RolesAllowed("admin")
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/admin")
    public ResponseEntity getAdminMessage() {
        return ResponseEntity.ok(new MessageDTO("for admin", 200));
    }

    @GetMapping(value = "/")
    public ResponseEntity getEmptyWithHeaderMessage() {
        return ResponseEntity.ok(new MessageDTO("empty", 200));
    }
}
