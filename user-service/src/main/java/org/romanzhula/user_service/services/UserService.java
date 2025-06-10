package org.romanzhula.user_service.services;

import org.romanzhula.user_service.dto.UserRequest;
import org.romanzhula.user_service.dto.UserResponse;
import org.romanzhula.user_service.dto.events.UserInfoMessage;


public interface UserService {

    UserResponse getUserById(Long id);
    UserResponse saveNewUser(UserRequest userRequest);

    void saveOrUpdateUser(UserInfoMessage userInfoMessage);

}
