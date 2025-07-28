/*
 * Spring Boot Security tutorial 
 * 
 * https://github.com/egalli64/spring-security
 */
package com.example.sec;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Spring Security Tutorial!");
        return "home";
    }

    @GetMapping("/public")
    public String publicEndpoint(Model model) {
        model.addAttribute("title", "A public page");
        model.addAttribute("content", "This page should be accessible to everyone");

        return "simple";
    }

    @GetMapping("/private")
    public String privateEndpoint(Model model) {
        model.addAttribute("title", "A private page");
        model.addAttribute("content", "This page should require authentication");

        return "simple";
    }

    @GetMapping("/admin")
    public String adminEndpoint(Model model) {
        model.addAttribute("title", "An admin page");
        model.addAttribute("content", "This page should require admin role");

        return "simple";
    }
}
