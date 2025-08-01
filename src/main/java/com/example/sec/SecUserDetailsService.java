/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */

package com.example.sec;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Manage the SecUser to UserDetails relation
 */
@Service
public class SecUserDetailsService implements UserDetailsService {
    private static final Logger log = LogManager.getLogger(SecUserDetailsService.class);

    private final UserRepository repo;

    public SecUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.traceEntry("User: {}", username);

        SecUser user = repo.findByUsername(username).orElseThrow(() -> {
            log.warn("User not found: {}", username);
            return new UsernameNotFoundException("User not found: " + username);
        });

        log.debug("Found user: {} with roles: {}", user.getUsername(), user.getRoles());

        // Convert business user to Security UserDetails
        return User.builder().username(user.getUsername()).password(user.getPassword())
                .authorities(mapRolesToAuthorities(user.getRoles())).accountExpired(user.isAccountExpired())
                .accountLocked(user.isAccountLocked()).credentialsExpired(user.isCredentialsExpired())
                .disabled(!user.isEnabled()).build();
    }

    /**
     * Convert role strings to Spring Security authorities
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(Collection<String> roles) {
        return roles.stream().map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new).<GrantedAuthority>map(x -> x).toList();
    }

    public UserRepository getRepo() {
        return repo;
    }
}
