package com.fooddelivery.controller;

import com.fooddelivery.model.User;
import com.fooddelivery.service.UserService;
import com.fooddelivery.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    // View profile
    @GetMapping("")
    public String viewProfile(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            // Refresh user data from database
            User user = userService.getUserById(currentUser.getId());
            session.setAttribute("currentUser", user);

            // Get order count
            int orderCount = orderService.getUserOrders(user.getId()).size();

            model.addAttribute("user", user);
            model.addAttribute("orderCount", orderCount);
            model.addAttribute("currentUser", user);
            return "profile";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load profile");
            model.addAttribute("user", currentUser);
            model.addAttribute("orderCount", 0);
            model.addAttribute("currentUser", currentUser);
            return "profile";
        }
    }

    // Update profile
    @PostMapping("/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam String address,
                                @RequestParam(required = false) String city,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            User updatedUser = new User();
            updatedUser.setId(currentUser.getId());
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            updatedUser.setPhone(phone);
            updatedUser.setAddress(address);
            updatedUser.setCity(city);

            User savedUser = userService.updateUser(currentUser.getId(), updatedUser);
            session.setAttribute("currentUser", savedUser);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
            return "redirect:/profile";
        }
    }
}