package com.example.taskapp.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;

    public enum Role {
        ADMIN, USER;
    }

    private String name;
    private String bio;
    private String photoUrl;
    @Lob
    private byte[] photo;
}
