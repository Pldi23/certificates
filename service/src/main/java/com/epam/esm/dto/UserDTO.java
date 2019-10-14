package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    @Null(message = "{violation.input.certificate.id}")
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\p{Punct})(?=\\S+$).{8,20}$")
    private String password;

    @NotNull
    @Pattern(regexp = "admin|user|guest")
    private String role;

    @Null
    @JsonIgnore
    private List<Long> ordersIds;
}
