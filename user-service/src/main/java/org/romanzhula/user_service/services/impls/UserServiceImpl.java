package org.romanzhula.user_service.services.impls;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.romanzhula.user_service.dto.SetPassphraseRequest;
import org.romanzhula.user_service.dto.UserRequest;
import org.romanzhula.user_service.dto.UserResponse;
import org.romanzhula.user_service.dto.VerifyPassphraseRequest;
import org.romanzhula.user_service.dto.events.UserInfoMessage;
import org.romanzhula.user_service.models.User;
import org.romanzhula.user_service.repositories.UserRepository;
import org.romanzhula.user_service.services.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    @Override
    @Transactional
    public void saveOrUpdateUser(UserInfoMessage userInfoMessage) {
        User user = userRepository.findByGoogleId(userInfoMessage.googleId())
                .orElseGet(() -> userRepository.findByGithubId(userInfoMessage.githubId()).orElse(null))
        ;

        if (user == null) {

            user = User.builder()
                    .name(userInfoMessage.name())
                    .email(userInfoMessage.email())
                    .googleId(userInfoMessage.googleId())
                    .githubId(userInfoMessage.githubId())
                    .build();
        } else {
            // If user exists - update it data
            user.setName(userInfoMessage.name());
            user.setEmail(userInfoMessage.email());

            // Update Google ID if it is missing
            if (userInfoMessage.googleId() != null && user.getGoogleId() == null) {
                user.setGoogleId(userInfoMessage.googleId());
            }

            // Update GitHub ID if it is missing
            if (userInfoMessage.githubId() != null && user.getGithubId() == null) {
                user.setGithubId(userInfoMessage.githubId());
            }
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void setPassphrase(SetPassphraseRequest request) {
        String googleId = request.getGoogleId();
        String githubId = request.getGithubId();

        User user = userRepository
                .findByGoogleId(googleId)
                .orElseGet(() -> userRepository.findByGithubId(githubId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found!"))
                )
        ;

        String passphraseHash = BCrypt.hashpw(request.getPassphrase(), BCrypt.gensalt());

        user.setPassphraseHash(passphraseHash);

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean verifyPassphrase(VerifyPassphraseRequest request) {
        Long userId = request.getUserId();

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"))
        ;

        return BCrypt.checkpw(request.getPassphrase(), user.getPassphraseHash());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserIdByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId)
                .map(User::getId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for googleId: " + googleId))
        ;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserIdByGithubId(String githubId) {
        return userRepository.findByGithubId(githubId)
                .map(User::getId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for githubId: " + githubId))
        ;
    }

}
