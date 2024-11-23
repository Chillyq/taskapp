package com.example.taskapp.controller;
import com.example.taskapp.entity.Task;
import com.example.taskapp.entity.User;
import com.example.taskapp.service.TaskService;
import com.example.taskapp.util.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;


@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    public TaskController(TaskService taskService, AuthenticatedUserProvider authenticatedUserProvider) {
        this.taskService = taskService;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider(); // Use the provider to get the user
        model.addAttribute("tasks", taskService.getAllTasksForUser(user));
        return "main";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "create-task"; // Thymeleaf template name
    }


    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider(); // Get logged-in user
        task.setUser(user); // Associate the task with the logged-in user
        taskService.createTask(task, null, user); // Save the task
        return "redirect:/tasks"; // Redirect to the dashboard
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        var taskOptional = taskService.getTaskById(id, user);

        if (taskOptional.isPresent()) {
            model.addAttribute("task", taskOptional.get());
            return "edit-task";
        } else {
            return "redirect:/tasks/main?error=TaskNotFound";
        }
    }


    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task updatedTask) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        taskService.updateTask(id, updatedTask, null, user);
        return "redirect:/tasks";
    }


    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        taskService.deleteTask(id, user);
        return "redirect:/tasks/main";
    }

    @GetMapping
    public String dashboard(Model model) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        model.addAttribute("tasks", taskService.getAllTasksForUser(user));
        return "dashboard"; // Thymeleaf template name for dashboard
    }

}