package ru.tinkoff.lab.tripAPI.security;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.lab.tripAPI.security.models.JwtAuthentication;
import ru.tinkoff.lab.tripAPI.security.models.LoginRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) throws AuthException {
        return authService.login(loginRequest);
    }

    @GetMapping("/hello/user")
    public String helloUser() {
        return "We got em4";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/hello/admin")
    public String helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return authInfo.getPrincipal().toString();
    }
}
