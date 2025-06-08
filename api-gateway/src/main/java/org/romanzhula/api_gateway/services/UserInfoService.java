package org.romanzhula.api_gateway.services;

import lombok.RequiredArgsConstructor;
import org.romanzhula.api_gateway.dto.UserInfoMessage;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final RabbitTemplate rabbitTemplate;


    public void processUserInfo(OAuth2AuthenticationToken token) {
        var userInfo = token.getPrincipal().getAttributes();
        String registrationId = token.getAuthorizedClientRegistrationId(); // Google or GitHub

        String name = (String) userInfo.get("name");
        String email = (String) userInfo.get("email");

        String googleId = null;
        String githubId = null;

        if ("google".equals(registrationId)) {
            googleId = (String) userInfo.get("sub");
        } else if ("github".equals(registrationId)) {
            githubId = userInfo.get("id").toString();
        }

        UserInfoMessage userInfoMessage = new UserInfoMessage(name, email, googleId, githubId);

        rabbitTemplate.convertAndSend("user-exchange", "user.routing.key", userInfoMessage);
    }

}
