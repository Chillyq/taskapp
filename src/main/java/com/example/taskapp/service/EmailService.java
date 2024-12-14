package com.example.taskapp.service;

import com.example.taskapp.entity.Task;
import com.example.taskapp.entity.User;
import com.example.taskapp.entity.Category;
import com.example.taskapp.repository.TaskRepo;
import com.example.taskapp.repository.UserRepo;
import com.example.taskapp.repository.CategRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("text");
        message.setFrom("akonyaku@gmail.com");
        mailSender.send(message);
    }

}
