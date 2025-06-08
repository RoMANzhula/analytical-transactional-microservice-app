package org.romanzhula.api_gateway.handlers;

import lombok.RequiredArgsConstructor;
import org.romanzhula.api_gateway.services.UserInfoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class OAuth2SuccessHandler implements ServerAuthenticationSuccessHandler {

    private final UserInfoService userInfoService;

    @Override
    public Mono<Void> onAuthenticationSuccess(
            WebFilterExchange webFilterExchange,
            Authentication authentication
    ) {

        if (authentication instanceof OAuth2AuthenticationToken token) {
            userInfoService.processUserInfo(token);
        }

        // redirect user to home page (or another URL)
        return webFilterExchange
                .getExchange()
                .getResponse()
                .setComplete()
        ;
    }

}
