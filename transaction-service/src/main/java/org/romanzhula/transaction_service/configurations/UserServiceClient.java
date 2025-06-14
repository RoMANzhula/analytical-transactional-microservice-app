package org.romanzhula.transaction_service.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient webClient;


    public boolean verifyPassphrase(String googleId, String githubId, String passphrase) {
        return webClient.post()
                .uri("/users/verify-passphrase")
                .bodyValue(Map.of(
                        "googleId", googleId,
                        "githubId", githubId,
                        "passphrase", passphrase
                ))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block()
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
