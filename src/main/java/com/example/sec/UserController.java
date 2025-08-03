/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
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

    private final SecUserService svc;

    public UserController(SecUserService svc) {
        this.svc = svc;
    }

    /**
     * List all users
     */
    @GetMapping
    public String list(Model model, RedirectAttributes attrs) {
        log.traceEntry("list()");

        try {
            model.addAttribute("title", "User Management");
            model.addAttribute("usernames", svc.getAllUsernames());
            model.addAttribute("userCount", svc.getSize());
            return "user-list";
        } catch (AccessDeniedException e) {
            log.warn("Can't list usernames", e);
            attrs.addFlashAttribute("errorMessage", "Access denied: Insufficient privileges");
            return "redirect:/";
        }
    }

    /**
     * Show user details
     */
    @GetMapping("/{username}")
    public String show(@PathVariable String username, Model model, RedirectAttributes attrs) {
        log.traceEntry("show({})", username);

        try {
            return svc.findByUsername(username).map(user -> {
                model.addAttribute("title", "User Details: " + username);
                model.addAttribute("user", user);
                return "user-details";
            }).orElseGet(() -> {
                log.info("User '{}' not found", username);
                attrs.addFlashAttribute("errorMessage", "User not found: " + username);
                return "redirect:/users";
            });
        } catch (AccessDeniedException e) {
            log.warn("Can't show user details", e);
            attrs.addFlashAttribute("errorMessage", "Access denied: Cannot view user details");
            return "redirect:/";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, RedirectAttributes attrs) {
        log.traceEntry("showCreateForm()");

        try {
            // accessing the service just to have a security check
            svc.requireAdminAccess();
            model.addAttribute("title", "Create New User");
            return "user-create";
        } catch (AccessDeniedException e) {
            log.warn("Can't show create form", e);
            attrs.addFlashAttribute("errorMessage", "Access denied: Admin privileges required");
            return "redirect:/";
        }
    }

    @PostMapping("/create")
    public String create(@RequestParam String username, @RequestParam String password,
            @RequestParam(required = false) boolean hasUserRole, @RequestParam(required = false) boolean hasAdminRole,
            RedirectAttributes attrs) {
        log.traceEntry("create({}, {}, {})", username, hasUserRole, hasAdminRole);

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
                log.warn("Can't create with no role");
                attrs.addFlashAttribute("errorMessage", "User must have at least one role");
                return "redirect:/users/create";
            }

            svc.create(username, password, roles);
            attrs.addFlashAttribute("successMessage", "User created successfully: " + username);
            return "redirect:/users";
        } catch (AccessDeniedException e) {
            log.warn("Can't create user", e);
            attrs.addFlashAttribute("errorMessage", "Access denied: Admin privileges required");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create user", e);
            attrs.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/create";
        }
    }

    @PostMapping("/{username}/toggle-enabled")
    public String toggleEnabled(@PathVariable String username, RedirectAttributes attrs) {
        log.traceEntry("toggleEnabled({})", username);

        try {
            return svc.findByUsername(username).map(user -> {
                boolean status = !user.isEnabled();
                svc.setEnabled(username, status);

                attrs.addFlashAttribute("successMessage",
                        "User " + username + " has been " + (status ? "enabled" : "disabled"));
                return "redirect:/users/" + username;
            }).orElseGet(() -> {
                attrs.addFlashAttribute("errorMessage", "User not found: " + username);
                return "redirect:/users";
            });
        } catch (AccessDeniedException e) {
            log.warn("Can't toggle enabled", e);
            attrs.addFlashAttribute("errorMessage", "Access denied: Admin privileges required");
            return "redirect:/";
        }
    }

    @PostMapping("/{username}/delete")
    public String delete(@PathVariable String username, RedirectAttributes attrs) {
        log.traceEntry("delete({})", username);
        try {
            if (svc.delete(username)) {
                attrs.addFlashAttribute("successMessage", "User deleted: " + username);
            } else {
                attrs.addFlashAttribute("errorMessage", "Failed to delete user: " + username);
            }

            return "redirect:/users";
        } catch (AccessDeniedException e) {
            log.warn("Can't delete user", e);
            attrs.addFlashAttribute("errorMessage", "Access denied: Admin privileges required");
            return "redirect:/";
        }
    }
}
