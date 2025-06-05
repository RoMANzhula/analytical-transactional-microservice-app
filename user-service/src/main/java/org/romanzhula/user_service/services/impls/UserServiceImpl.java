package org.romanzhula.user_service.services.impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.romanzhula.user_service.dto.UserResponse;
import org.romanzhula.user_service.models.User;
import org.romanzhula.user_service.repositories.UserRepository;
import org.romanzhula.user_service.services.UserService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with ID:" + id + " NOT FOUND"
                ))
        ;

        return modelMapper.map(user, UserResponse.class);
    }

}
