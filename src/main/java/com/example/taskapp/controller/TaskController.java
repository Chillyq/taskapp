package com.example.taskapp.controller;
import com.example.taskapp.entity.Task;
import com.example.taskapp.entity.User;
import com.example.taskapp.service.TaskService;
import com.example.taskapp.service.CategoryService;
import com.example.taskapp.util.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final CategoryService categoryService;

    @Autowired
    public TaskController(TaskService taskService, AuthenticatedUserProvider authenticatedUserProvider, CategoryService categoryService) {
        this.taskService = taskService;
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.categoryService = categoryService;
    }

    @GetMapping("/main")
    public String listTasks(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.getTasks(pageable);

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        return "main";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "create-task";
    }


    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task, @RequestParam Long categoryId) {
        task.setCategory(categoryService.getCategoryById(categoryId));
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        task.setUser(user);
        taskService.createTask(task, categoryId, user);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        var taskOptional = taskService.getTaskById(id, user);

        if (taskOptional.isPresent()) {
            model.addAttribute("task", taskOptional.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit-task";
        } else {
            return "redirect:/tasks/main?error=TaskNotFound";
        }
    }


    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task updatedTask, @RequestParam Long categoryId) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        updatedTask.setCategory(categoryService.getCategoryById(categoryId));
        taskService.updateTask(id, updatedTask, categoryId, user);
        return "redirect:/tasks";
    }


    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        taskService.deleteTask(id, user);
        return "redirect:/tasks";
    }

    @GetMapping
    public String dashboard(Model model) {
        var user = authenticatedUserProvider.getAuthenticatedUserProvider();
        model.addAttribute("tasks", taskService.getAllTasksForUser(user));
        return "dashboard";
    }

    @GetMapping("/paginated")
    public String showDashboard(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Task> taskPage = taskService.getTasksWithPagination(page, 10);
        System.out.println("Total pages: " + taskPage.getTotalPages());
        System.out.println("Tasks: " + taskPage.getContent());

        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "dashboard";
    }

}



