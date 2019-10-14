package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchDTO {

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\p{Punct})(?=\\S+$).{8,20}$")
    private String password;

    @Pattern(regexp = "admin|user|guest")
    private String role;
}
