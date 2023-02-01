package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String listUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("principal", principal);
        return "users";
    }

    @PostMapping("/admin")
    public String addUser(@ModelAttribute("user") User user) {
        if (user.getRoles().isEmpty()) {
             user.addRole(roleService.getRoleByName("ROLE_USER"));
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/create")
    public String addUsers(Model model, Principal principal) {
        User user = new User();
        if (roleService.getRoles().isEmpty()) {
            roleService.addRole(new Role("ROLE_USER"));
            roleService.addRole(new Role("ROLE_ADMIN"));
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("principal", principal);
        return "create_user";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("principal", principal);
        return "edit_user";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User user,
                             Model model) {
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
