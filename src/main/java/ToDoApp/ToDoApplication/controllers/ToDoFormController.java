package ToDoApp.ToDoApplication.controllers;

import ToDoApp.ToDoApplication.models.ToDoItem;
import ToDoApp.ToDoApplication.models.User;
import ToDoApp.ToDoApplication.services.ToDoItemService;
import ToDoApp.ToDoApplication.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToDoFormController {
    @Autowired
    private UserService userService;

    @Autowired
    private ToDoItemService toDoItemService;

    @GetMapping("/")
    public ModelAndView index(@AuthenticationPrincipal UserDetails user) {
        System.out.println("Home page loaded");
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", user);
        modelAndView.addObject("toDoItems", toDoItemService.getAllByOwnerId(userService.findByUsername(user.getUsername()).getId()));
        return modelAndView;
    }

    @GetMapping("/create-todo")
    public String showCreateForm(ToDoItem toDoItem) {
        return "new-todo-item";
    }

    @PostMapping("/todo")
    public String createToDoItem(@AuthenticationPrincipal UserDetails user, @Valid ToDoItem toDoItem, BindingResult result, Model model) {
        System.out.println("Add new todo item");
        ToDoItem item = new ToDoItem();
        item.setDescription(toDoItem.getDescription());
        item.setIsComplete(toDoItem.getIsComplete());
        item.setOwnerId(
                userService.findByUsername(user.getUsername()).getId()
        );
        toDoItemService.save(item);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteToDoItem(@PathVariable("id") Long id, Model model) {
        System.out.println("Delete todo item");
        ToDoItem toDoItem = toDoItemService
                .getById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));
        toDoItemService.delete(toDoItem);
        return "redirect:/";
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        System.out.println("Edit todo item");
        ToDoItem toDoItem = toDoItemService
                .getById(id)
                .orElseThrow(() -> new IllegalArgumentException("ToDoItem id: " + id + " not found"));
        model.addAttribute("todo", toDoItem);
        return "edit-todo-item";
    }

    @PostMapping("/todo/{id}")
    public String updateTodoItem(@PathVariable("id") Long id, @Valid ToDoItem toDoItem, BindingResult result, Model model) {
        System.out.println("Todo item edited");
        ToDoItem item = toDoItemService
                .getById(id)
                .orElseThrow(() -> new IllegalArgumentException("ToDoItem id: " + id + " not found"));
        item.setIsComplete(toDoItem.getIsComplete());
        item.setDescription(toDoItem.getDescription());

        toDoItemService.save(item);
        return "redirect:/";
    }
}
