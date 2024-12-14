package com.example.taskapp.controller;
import com.example.taskapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPasswordController {
    @Autowired
    private UserService userService;

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        if (!userService.isResetTokenValid(token)) {
            return "redirect:/login?error=InvalidToken";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!userService.isResetTokenValid(token)) {
            return "redirect:/login?error=InvalidToken";
        }
        userService.resetPassword(token, newPassword);
        return "redirect:/login?resetSuccess=true";
    }
}
