package org.romanzhula.user_service.services;

import org.romanzhula.user_service.dto.UserRequest;
import org.romanzhula.user_service.dto.UserResponse;


public interface UserService {

    UserResponse getUserById(Long id);
    UserResponse saveNewUser(UserRequest userRequest);

}
