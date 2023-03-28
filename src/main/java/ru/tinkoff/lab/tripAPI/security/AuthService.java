package ru.tinkoff.lab.tripAPI.security;

import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.lab.tripAPI.business.User;
import ru.tinkoff.lab.tripAPI.business.service.UserService;
import ru.tinkoff.lab.tripAPI.security.models.JwtAuthentication;
import ru.tinkoff.lab.tripAPI.security.models.LoginRequest;
import ru.tinkoff.lab.tripAPI.security.utils.JwtProvider;
import ru.tinkoff.lab.tripAPI.security.utils.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public String login(@NonNull LoginRequest authRequest) throws AuthException {
        final User user = userService.findByEmail(authRequest.getEmail());
        String password = passwordEncoder.generatePassword(authRequest.getPassword(), user.getSalt());
        if (password.equals(user.getPassword())) {
            return jwtProvider.generateAccessToken(user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong password");
        }
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}