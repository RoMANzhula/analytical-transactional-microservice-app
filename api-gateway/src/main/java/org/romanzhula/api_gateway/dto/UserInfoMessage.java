package org.romanzhula.api_gateway.dto;


public record UserInfoMessage(

        String name,
        String email,
        String googleId,
        String githubId

) {}
