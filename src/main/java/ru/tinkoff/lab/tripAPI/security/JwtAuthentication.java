package ru.tinkoff.lab.tripAPI.security;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.tinkoff.lab.tripAPI.security.models.Role;

import java.util.Collection;
import java.util.Set;

@Data
public class JwtAuthentication implements Authentication {

    private boolean authenticated;
    private String username;
    private String firstName;
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return roles; }

    @Override
    public Object getCredentials() { return username; }

    @Override
    public Object getDetails() { return null; }

    @Override
    public Object getPrincipal() { return username; }

    @Override
    public boolean isAuthenticated() { return authenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() { return firstName; }
}