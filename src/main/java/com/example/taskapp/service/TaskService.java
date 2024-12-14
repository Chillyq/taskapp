package com.example.taskapp.service;
import com.example.taskapp.entity.Task;
import com.example.taskapp.entity.User;
import com.example.taskapp.entity.Category;
import com.example.taskapp.repository.TaskRepo;
import com.example.taskapp.repository.UserRepo;
import com.example.taskapp.repository.CategRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final CategRepo categRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo, UserRepo userRepo, CategRepo categRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.categRepo = categRepo;
    }

    public Page<Task> getTasks(Pageable pageable) {
        return taskRepo.findAll(pageable);
    }

    public Page<Task> getTasksWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepo.findAll(pageable);
    }

    public List<Task> getAllTasksForUser(User user) {
        return taskRepo.findByUser(user);
    }

    public Optional<Task> getTaskById(Long id, User user) {
        return taskRepo.findByIdAndUser(id, user);
    }


    @Transactional
    public Task createTask(Task task, Long categoryId, User user) {
        task.setUser(user);

        if (categoryId != null) {
            Category category = categRepo.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
            task.setCategory(category);
        }

        return taskRepo.save(task);
    }

    @Transactional
    public Task updateTask(Long id, Task updatedTask, Long categoryId, User user) {
        Task existingTask = taskRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setPriority(updatedTask.getPriority());

        if (categoryId != null) {
            Category category = categRepo.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
            existingTask.setCategory(category);
        } else {
            existingTask.setCategory(null);
        }

        return taskRepo.save(existingTask);
    }

    @Transactional
    public void deleteTask(Long id, User user) {
        Task task = taskRepo.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Task not found or not owned by user"));
        taskRepo.delete(task);
    }
}