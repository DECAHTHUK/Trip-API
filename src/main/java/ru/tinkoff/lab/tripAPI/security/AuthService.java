package ru.tinkoff.lab.tripAPI.security;

import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;
import ru.tinkoff.lab.tripAPI.security.models.LoginRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public String login(@NonNull LoginRequest authRequest) throws AuthException {
        final User user = userService.findByEmail(authRequest.getEmail());
        //TODO: add salt hashing
        if (user.getPassword().equals(authRequest.getPassword())) {
            return jwtProvider.generateAccessToken(user);
        } else {
            throw new AuthException("Wrong password");
        }
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}