/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * User manager - to be replaced with a JPA repository
 */
@Repository
public class UserRepository {

    private final Map<String, SecUser> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public UserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        init();
    }

    /**
     * Some default users
     */
    private void init() {
        create("user", "password", Set.of("USER"));
        create("admin", "admin", Set.of("ADMIN"));
        create("superuser", "super", Set.of("USER", "ADMIN"));

        SecUser disabled = new SecUser("disabled", passwordEncoder.encode("disabled"), Set.of("USER"));
        disabled.setEnabled(false);
        users.put("disabled", disabled);
    }

    public SecUser create(String username, String password, Set<String> roles) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("User already exists: " + username);
        }

        SecUser user = new SecUser(username, passwordEncoder.encode(password), roles);
        users.put(username, user);
        return user;
    }

    public Optional<SecUser> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public boolean updatePassword(String username, String password) {
        SecUser user = users.get(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            return true;
        }
        return false;
    }

    public boolean updateRoles(String username, Set<String> newRoles) {
        SecUser user = users.get(username);
        if (user != null) {
            user.setRoles(newRoles);
            return true;
        }
        return false;
    }

    public boolean setEnabled(String username, boolean enabled) {
        SecUser user = users.get(username);
        if (user != null) {
            user.setEnabled(enabled);
            return true;
        }
        return false;
    }

    public boolean delete(String username) {
        return users.remove(username) != null;
    }

    public Set<String> getAllUsernames() {
        return users.keySet();
    }

    public int getSize() {
        return users.size();
    }
}
