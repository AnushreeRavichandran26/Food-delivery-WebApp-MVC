package com.fooddelivery.controller;

import com.fooddelivery.model.User;
import com.fooddelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    // Landing Page
    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    // Login Page
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "login";
    }

    // Handle Login
    @PostMapping("/perform_login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = userService.loginUser(email, password);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                session.setAttribute("currentUser", user);
                session.setAttribute("userId", user.getId());
                return "redirect:/home";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid credentials");
                return "redirect:/login?error=true";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/login?error=true";
        }
    }

    // Signup Page
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // Handle Signup
    @PostMapping("/signup")
    public String handleSignup(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String address,
                               @RequestParam(required = false) String city,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setAddress(address);
            user.setCity(city);
            user.setPassword(password);

            User newUser = userService.registerUser(user);
            session.setAttribute("currentUser", newUser);
            session.setAttribute("userId", newUser.getId());
            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Signup failed: " + e.getMessage());
            return "redirect:/signup";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}