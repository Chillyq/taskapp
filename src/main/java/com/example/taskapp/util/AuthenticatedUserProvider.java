package com.example.taskapp.util;
import com.example.taskapp.entity.User;
import com.example.taskapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

    private final UserRepo userRepository;

    @Autowired
    public AuthenticatedUserProvider(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUserProvider() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username);
    }
}
