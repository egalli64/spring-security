/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecUserService {
    private final SecUserRepository userRepository;
    private final SecRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public SecUserService(SecUserRepository userRepository, SecRoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SecUser create(String username, String password, Set<String> roleNames) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User already exists: " + username);
        }

        Set<SecRole> roles = new HashSet<>();
        for (String roleName : roleNames) {
            SecRole role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
            roles.add(role);
        }

        SecUser user = new SecUser(username, passwordEncoder.encode(password), roles);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<SecUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean updatePassword(String username, String password) {
        Optional<SecUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            SecUser user = userOpt.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean updateRoles(String username, Set<String> newRoleNames) {
        Optional<SecUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            SecUser user = userOpt.get();

            Set<SecRole> newRoles = new HashSet<>();
            for (String roleName : newRoleNames) {
                SecRole role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
                newRoles.add(role);
            }

            user.setRoles(newRoles);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean setEnabled(String username, boolean enabled) {
        Optional<SecUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            SecUser user = userOpt.get();
            user.setEnabled(enabled);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean delete(String username) {
        Optional<SecUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Set<String> getAllUsernames() {
        return userRepository.findAllUsernames();
    }

    @Transactional(readOnly = true)
    public long getSize() {
        return userRepository.count();
    }
}
