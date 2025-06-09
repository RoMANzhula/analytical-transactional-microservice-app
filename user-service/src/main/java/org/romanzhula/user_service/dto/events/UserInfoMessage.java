package org.romanzhula.user_service.dto.events;

public record UserInfoMessage(

        String name,
        String email,
        String googleId,
        String githubId

) {
}
