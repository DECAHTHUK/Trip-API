package ru.tinkoff.lab.tripAPI.security.utils;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.tinkoff.lab.tripAPI.security.models.JwtAuthentication;
import ru.tinkoff.lab.tripAPI.security.models.Role;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(Set.of(Role.valueOf(claims.get("role", String.class))));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }
}