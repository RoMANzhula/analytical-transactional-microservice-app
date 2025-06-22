package org.romanzhula.api_gateway.handlers;

import lombok.RequiredArgsConstructor;
import org.romanzhula.api_gateway.services.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements ServerAuthenticationSuccessHandler {

    private final UserInfoService userInfoService;

    @Override
    public Mono<Void> onAuthenticationSuccess(
            WebFilterExchange webFilterExchange,
            Authentication authentication
    ) {

        if (authentication instanceof OAuth2AuthenticationToken token) {
            var userInfo = token.getPrincipal().getAttributes();
            String googleId = (String) userInfo.get("sub");
            String githubId = null;

            if ("github".equals(token.getAuthorizedClientRegistrationId())) {
                githubId = userInfo.get("id").toString();
            }

            userInfoService.processUserInfo(token);

            // redirect user to set-passphrase page
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.FOUND); // redirect 302

            String redirectUrl = UriComponentsBuilder
                    .fromUriString("http://localhost:3000/set-passphrase")
                    .queryParam("googleId", googleId)
                    .queryParam("githubId", githubId)
                    .build()
                    .toUriString()
            ;

            response.getHeaders().setLocation(java.net.URI.create(redirectUrl));

            return response.setComplete();
        }

        return Mono.error(new RuntimeException("Invalid authentication token."));
    }

}
