package com.example.taskapp.controller;

import com.example.taskapp.entity.Task;
import com.example.taskapp.entity.User;
import com.example.taskapp.repository.UserRepo;
import com.example.taskapp.service.TaskService;
import com.example.taskapp.service.CategoryService;
import com.example.taskapp.service.UserService;
import com.example.taskapp.util.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/tasks/profile")
public class ProfileController {
    private final UserService userService;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserRepo userRepo;
    @Autowired
    public ProfileController(UserService userService, AuthenticatedUserProvider authenticatedUserProvider, UserRepo userRepo) {
        this.userService = userService;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String getProfile(Model model) {
        User user = authenticatedUserProvider.getAuthenticatedUserProvider();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(String username, String email, MultipartFile photo) {
        userService.updateUserProfile(username, email, photo);
        return "redirect:/profile?success=true";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword) {
        User user = authenticatedUserProvider.getAuthenticatedUserProvider();
        userService.changePassword(user, newPassword);
        return "redirect:/profile?passwordChanged=true";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email) {
        userService.resetPassword(email);
        return "redirect:/login?reset=true";
    }

    @GetMapping("/photos/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(user.getPhoto());
    }

}
