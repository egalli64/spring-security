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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AppController {
    private static final Logger log = LogManager.getLogger(AppController.class);

    @GetMapping("/")
    public String home(Model model) {
        log.traceEntry("home()");

        model.addAttribute("message", "Welcome to Spring Security Tutorial!");
        return "home";
    }

    @GetMapping("/public")
    public String publicEndpoint(Model model) {
        log.traceEntry("publicEndpoint()");

        model.addAttribute("title", "A public page");
        model.addAttribute("content", "This page should be accessible to everyone");

        return "simple";
    }

    @GetMapping("/private")
    public String privateEndpoint(Model model) {
        log.traceEntry("privateEndpoint()");

        model.addAttribute("title", "A private page");
        model.addAttribute("content", "This page should require authentication");

        return "simple";
    }

    @GetMapping("/admin")
    public String adminEndpoint(Model model) {
        log.traceEntry("adminEndpoint()");

        model.addAttribute("title", "An admin page");
        model.addAttribute("content", "This page should require admin role");

        return "simple";
    }

    /**
     * Custom login page
     */
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        log.traceEntry("login({})", error);

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }

        return "login";
    }
}
