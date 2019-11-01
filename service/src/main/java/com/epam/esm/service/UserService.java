package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;


public interface UserService extends UpdateService<UserDTO>, SaveService<UserDTO>, PatchService<UserDTO, UserPatchDTO>,
        FindOneService<UserDTO>, FindAllService<UserDTO>, DeleteService {
}
