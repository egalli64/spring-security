/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

/**
 * Controller for user management operations (admin only)
 */
@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    private final UserRepository userStore;

    public UserController(UserRepository userStore) {
        this.userStore = userStore;
    }

    /**
     * List all users
     */
    @GetMapping
    public String list(Model model) {
        log.traceEntry();

        model.addAttribute("title", "User Management");
        model.addAttribute("usernames", userStore.getAllUsernames());
        model.addAttribute("userCount", userStore.getSize());

        return "user-list";
    }

    /**
     * Show user details
     */
    @GetMapping("/{username}")
    public String show(@PathVariable String username, Model model, RedirectAttributes attrs) {
        log.traceEntry("username: {}", username);

        return userStore.findByUsername(username).map(user -> {
            model.addAttribute("title", "User Details: " + username);
            model.addAttribute("user", user);
            return "user-details";
        }).orElseGet(() -> {
            attrs.addFlashAttribute("errorMessage", "User not found: " + username);
            return "redirect:/users";
        });
    }

    /**
     * Show create user form
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        log.traceEntry();

        model.addAttribute("title", "Create New User");
        return "user-create";
    }

    /**
     * Create new user
     */
    @PostMapping("/create")
    public String create(@RequestParam String username, @RequestParam String password,
            @RequestParam(required = false) boolean hasUserRole, @RequestParam(required = false) boolean hasAdminRole,
            RedirectAttributes redirectAttributes) {
        log.traceEntry("username: {}, hasUserRole: {}, hasAdminRole: {}", username, hasUserRole, hasAdminRole);

        try {
            Set<String> roles = Set.of();
            if (hasUserRole && hasAdminRole) {
                roles = Set.of("USER", "ADMIN");
            } else if (hasUserRole) {
                roles = Set.of("USER");
            } else if (hasAdminRole) {
                roles = Set.of("ADMIN");
            }

            if (roles.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User must have at least one role");
                return "redirect:/users/create";
            }

            userStore.create(username, password, roles);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully: " + username);
            return "redirect:/users";

        } catch (IllegalArgumentException e) {
            log.warn("Failed to create user: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/create";
        }
    }

    /**
     * Toggle user enabled status
     */
    @PostMapping("/{username}/toggle-enabled")
    public String toggleEnabled(@PathVariable String username, RedirectAttributes attrs) {
        log.traceEntry("username: {}", username);

        return userStore.findByUsername(username).map(user -> {
            boolean status = !user.isEnabled();
            userStore.setEnabled(username, status);

            attrs.addFlashAttribute("successMessage",
                    "User " + username + " has been " + (status ? "enabled" : "disabled"));
            return "redirect:/users/" + username;
        }).orElseGet(() -> {
            attrs.addFlashAttribute("errorMessage", "User not found: " + username);
            return "redirect:/users";
        });
    }

    /**
     * Delete user
     */
    @PostMapping("/{username}/delete")
    public String delete(@PathVariable String username, RedirectAttributes attrs) {
        log.traceEntry("username: {}", username);

        if (userStore.delete(username)) {
            attrs.addFlashAttribute("successMessage", "User deleted: " + username);
        } else {
            attrs.addFlashAttribute("errorMessage", "Failed to delete user: " + username);
        }

        return "redirect:/users";
    }
}
