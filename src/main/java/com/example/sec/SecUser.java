/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import java.util.Set;

/**
 * Security User
 */
public class SecUser {
    private String username;
    private String password;
    private Set<String> roles;
    private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;

    public SecUser() {
        this.enabled = true;
        this.accountExpired = false;
        this.accountLocked = false;
        this.credentialsExpired = false;
    }

    public SecUser(String username, String password, Set<String> roles) {
        this();
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Canonical constructor
    public SecUser(String username, String password, Set<String> roles, boolean enabled, boolean accountExpired,
            boolean accountLocked, boolean credentialsExpired) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    @Override
    public String toString() {
        return "User{" +  username + ", roles=" + roles + ", enabled=" + enabled + '}';
    }
}
