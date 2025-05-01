package com.example.starwars.star_wars.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Return the login HTML page
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // Logic for user authentication
        if (isValidUser(username, password)) {
            // On successful authentication, redirect to /search
            return "redirect:/search";
        } else {
            // Return to login page with error
            return "login?error";
        }
    }

    private boolean isValidUser(String username, String password) {
        // Add actual user validation logic (e.g., checking database or static values)
        return "admin".equals(username) && "password".equals(password);
    }
}
