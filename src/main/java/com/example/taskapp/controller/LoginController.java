package com.example.taskapp.controller;

import com.example.taskapp.entity.User;
import com.example.taskapp.util.AuthenticatedUserProvider;
import com.example.taskapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.taskapp.util.AuthenticatedUserProvider;


@Controller
public class LoginController {

    @Autowired
    private UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public LoginController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        User user = userRepo.findByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }

        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        username, password, java.util.Collections.emptyList()
                )
        );

        return "redirect:/tasks";
    }

}