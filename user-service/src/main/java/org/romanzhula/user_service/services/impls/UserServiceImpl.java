package org.romanzhula.user_service.services.impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.romanzhula.user_service.dto.UserRequest;
import org.romanzhula.user_service.dto.UserResponse;
import org.romanzhula.user_service.models.User;
import org.romanzhula.user_service.repositories.UserRepository;
import org.romanzhula.user_service.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with ID:" + id + " NOT FOUND"
                ))
        ;

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    @Transactional
    public UserResponse saveNewUser(UserRequest userRequest) {
        User savedUser = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build()
        ;

        userRepository.save(savedUser);

        return modelMapper.map(savedUser, UserResponse.class);
    }

}
