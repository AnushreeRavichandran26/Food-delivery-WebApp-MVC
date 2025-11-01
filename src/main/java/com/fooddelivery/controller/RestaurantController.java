package com.fooddelivery.controller;

import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.User;
import com.fooddelivery.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // Home page - List all restaurants
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("currentUser", currentUser);
        return "home";
    }

    // View restaurant menu
    // ✅ Return Menu Items as JSON (for popup modal)
    @GetMapping("/restaurants/{id}/menu")
    @ResponseBody
    public List<MenuItem> getMenuItems(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return null; // or throw unauthorized
        }
        return restaurantService.getRestaurantMenu(id);
    }

    // ✅ Return Restaurant details as JSON
    @GetMapping("/restaurants/{id}")
    @ResponseBody
    public Restaurant getRestaurant(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return null; // or throw unauthorized
        }
        return restaurantService.getRestaurantById(id);
    }



    // Search restaurants by cuisine
    @GetMapping("/restaurants/search")
    public String searchByCuisine(@RequestParam String cuisine, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Restaurant> restaurants = restaurantService.getRestaurantsByCuisine(cuisine);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("searchCuisine", cuisine);
        return "home";
    }
}