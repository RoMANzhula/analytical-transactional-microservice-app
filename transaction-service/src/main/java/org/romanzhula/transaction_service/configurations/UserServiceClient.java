package org.romanzhula.transaction_service.configurations;

import lombok.RequiredArgsConstructor;
import org.romanzhula.transaction_service.dto.VerifyPassphraseRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient webClient;


    public boolean verifyPassphrase(String googleId, String githubId, String passphrase) {
        Long userId = null;

        if (googleId != null) {
            userId = getUserIdByGoogleId(googleId);
        } else if (githubId != null) {
            userId = getUserIdByGithubId(githubId);
        }

        if (userId == null) {
            throw new IllegalArgumentException("User ID not found via Google or GitHub ID");
        }

        VerifyPassphraseRequestDto requestDto = new VerifyPassphraseRequestDto(userId, passphrase);

        return Boolean.TRUE.equals(webClient.post()
                .uri("/api/v1/users/verify-passphrase")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block())
        ;
    }

    public Long getUserIdByGoogleId(String googleId) {
        return webClient.get()
                .uri("/api/v1/users/id/google")
                .header("X-Google-Id", googleId)
                .retrieve()
                .bodyToMono(Long.class)
                .block()
        ;
    }


    public Long getUserIdByGithubId(String githubId) {
        return webClient.get()
                .uri("/api/v1/users/id/github")
                .header("X-Github-Id", githubId)
                .retrieve()
                .bodyToMono(Long.class)
                .block()
        ;
    }

}
