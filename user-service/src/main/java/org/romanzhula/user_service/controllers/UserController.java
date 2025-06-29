package org.romanzhula.user_service.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.romanzhula.user_service.dto.SetPassphraseRequest;
import org.romanzhula.user_service.dto.UserRequest;
import org.romanzhula.user_service.dto.UserResponse;
import org.romanzhula.user_service.dto.VerifyPassphraseRequest;
import org.romanzhula.user_service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid UserRequest userRequest
    ) {
        return ResponseEntity.ok(userService.saveNewUser(userRequest));
    }

    // after login via OAuth2, the user must add their own secret passphrase to confirm the transaction in the future
    // from client side we get passphrase and googleId or githubId
    @PostMapping("/set-passphrase")
    public ResponseEntity<String> setPassphrase(
            @RequestBody @Valid SetPassphraseRequest request
    ) {
        userService.setPassphrase(request);

        return ResponseEntity.ok("Passphrase set successfully!");
    }

    // for create transactions user must input a passphrase
    @PostMapping("/verify-passphrase")
    public ResponseEntity<Boolean> verifyPassphrase(
            @RequestBody @Valid VerifyPassphraseRequest request
    ) {
        return ResponseEntity.ok(userService.verifyPassphrase(request));
    }

    @GetMapping("/id/google")
    public ResponseEntity<Long> getUserIdByGoogleId(
            @RequestHeader("X-Google-Id") String googleId
    ) {
        Long userId = userService.getUserIdByGoogleId(googleId);
        if (userId == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(userId);
    }

    @GetMapping("/id/github")
    public ResponseEntity<Long> getUserIdByGithubId(
            @RequestHeader("X-Github-Id") String githubId
    ) {
        Long userId = userService.getUserIdByGithubId(githubId);
        if (userId == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(userId);
    }

}
