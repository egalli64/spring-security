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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * No local handling for AccessDeniedException - use global handler
 */
@Controller
@RequestMapping("/users")
public class UserViewController {
    private static final Logger log = LogManager.getLogger(UserViewController.class);
    private final SecUserService svc;

    public UserViewController(SecUserService svc) {
        this.svc = svc;
    }

    /**
     * List all users
     */
    @GetMapping
    public String list(Model model) {
        log.traceEntry("list()");

        model.addAttribute("title", "User Management");
        model.addAttribute("usernames", svc.getAllUsernames());
        model.addAttribute("userCount", svc.getSize());
        return "user-list";
    }

    /**
     * Show user details
     */
    @GetMapping("/{username}")
    public String show(@PathVariable String username, Model model, RedirectAttributes attrs) {
        log.traceEntry("show({})", username);

        return svc.findByUsername(username).map(user -> {
            model.addAttribute("title", "User Details: " + username);
            model.addAttribute("user", user);
            return "user-details";
        }).orElseGet(() -> {
            log.info("User '{}' not found", username);
            attrs.addFlashAttribute("errorMessage", "User not found: " + username);
            return "redirect:/users";
        });
    }
}
