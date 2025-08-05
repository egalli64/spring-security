/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller for user management operations (admin only)
 * <p>
 * Local AccessDeniedException, see @ExceptionHandler
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
     * Local exception handler - overrides global handler for this controller
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String handleLocalAccessDenied(AccessDeniedException e, HttpServletRequest request,
            RedirectAttributes attrs) {
        String uri = request.getRequestURI();
        log.warn("Local handler: Access denied for {} - {}", uri, e.getMessage());

        if (uri.contains("/create")) {
            attrs.addFlashAttribute("errorMessage", "Cannot create users - Admin privileges required");
            return "redirect:/users/create";
        } else if (uri.contains("/delete")) {
            attrs.addFlashAttribute("errorMessage", "Cannot delete users - Admin privileges required");
        } else {
            attrs.addFlashAttribute("errorMessage", "Cannot access user management - Admin privileges required");
        }

        return "redirect:/users";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, RedirectAttributes attrs) {
        log.traceEntry("showCreateForm()");

        // this call triggers security check
        svc.requireAdminAccess();
        model.addAttribute("title", "Create New User");
        return "user-create";
    }

    /**
     * Method level AccessDeniedException handling
     */
    @PostMapping("/create")
    public String create(@RequestParam String username, @RequestParam String password,
            @RequestParam(required = false) boolean hasUserRole, //
            @RequestParam(required = false) boolean hasAdminRole,
            @RequestParam(required = false) boolean hasViewReportAuth,
            @RequestParam(required = false) boolean hasEditPostsAuth, //
            RedirectAttributes attrs) {
        log.traceEntry("create({}, {}, {}, {}, {})", //
                username, hasUserRole, hasAdminRole, hasViewReportAuth, hasEditPostsAuth);

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

            Set<String> auths = Set.of();
            if (hasViewReportAuth && hasEditPostsAuth) {
                roles = Set.of("VIEW_REPORTS", "EDIT_POSTS");
            } else if (hasViewReportAuth) {
                roles = Set.of("VIEW_REPORTS");
            } else if (hasEditPostsAuth) {
                roles = Set.of("EDIT_POSTS");
            }

            svc.create(username, password, roles, auths);
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
    }

    @PostMapping("/{username}/delete")
    public String delete(@PathVariable String username, RedirectAttributes attrs) {
        log.traceEntry("delete({})", username);

        if (svc.delete(username)) {
            attrs.addFlashAttribute("successMessage", "User deleted: " + username);
        } else {
            attrs.addFlashAttribute("errorMessage", "Failed to delete user: " + username);
        }

        return "redirect:/users";
    }
}
