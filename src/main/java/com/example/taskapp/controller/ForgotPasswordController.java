package com.example.taskapp.controller;
import com.example.taskapp.entity.User;
import com.example.taskapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email) {
        User user = userService.findByEmail(email);
        String resetToken = userService.generateResetTokenForUser(user);

        String resetUrl = "http://localhost:8060/reset-password?token=" + resetToken;
        sendResetEmail(email, resetUrl);

        return "redirect:/login?resetEmailSent=true";
    }

    private void sendResetEmail(String recipientEmail, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the following link: " + resetUrl);
        mailSender.send(message);
    }
}
