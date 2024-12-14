package com.example.taskapp.service;

import com.example.taskapp.entity.User;
import com.example.taskapp.repository.UserRepo;
import com.example.taskapp.util.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    private final Map<String, String> resetTokenStore = new HashMap<>();
    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailService emailService, AuthenticatedUserProvider authenticatedUserProvider) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public String generateResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        resetTokenStore.put(token, user.getEmail());
        return token;
    }

    public boolean isResetTokenValid(String token) {
        return resetTokenStore.containsKey(token);
    }

    public void resetPassword(String token, String newPassword) {
        String email = resetTokenStore.get(token);
        User user = findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        resetTokenStore.remove(token);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void updateUserProfile(String username, String email, MultipartFile photo) {
        User user = authenticatedUserProvider.getAuthenticatedUserProvider();
        user.setUsername(username);
        user.setEmail(email);

        if (!photo.isEmpty()) {
            try {
                user.setPhoto(photo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload photo", e);
            }
        }

        userRepo.save(user);
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    public void resetPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        String newPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        emailService.sendEmail(email, "Password Reset",
                "Your new password is: " + newPassword);
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
