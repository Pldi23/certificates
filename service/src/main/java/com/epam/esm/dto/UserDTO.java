package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonIgnoreProperties(value={ "password" }, allowSetters=true)
public class UserDTO {

    @Null(message = "{violation.input.certificate.id}")
    private Long id;

    @NotBlank(message = "{email should be not blank}")
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\p{Punct})(?=\\S+$).{8,20}$", message = "{violation.password}")
    private String password;

    @NotNull(message = "{role should be not blank}")
    @Pattern(regexp = "ROLE_ADMIN|ROLE_USER|ROLE_GUEST", message = "{violation.role}")
    private String role;

    @Null
    @JsonIgnore
    private List<Long> ordersIds;
}
