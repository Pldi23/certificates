package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
public interface UserService extends UpdateService<UserDTO>, SaveService<UserDTO>, PatchService<UserDTO, UserPatchDTO>,
FindOneService<UserDTO>, FindAllService<UserDTO>, DeleteService {

    Optional<User> findByEmail(String email);
    long count();
//    Optional<UserDTO> findByEmail(String email);
}
