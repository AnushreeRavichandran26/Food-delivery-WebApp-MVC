package com.fooddelivery.controller;

import com.fooddelivery.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    // Add item to cart
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam Long itemId,
                                         @RequestParam String itemName,
                                         @RequestParam Double price,
                                         @RequestParam Long restaurantId,
                                         @RequestParam String restaurantName,
                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

            if (cart == null) {
                cart = new ArrayList<>();
            }

            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("id", itemId);
            cartItem.put("name", itemName);
            cartItem.put("price", price);
            cartItem.put("restaurantId", restaurantId);
            cartItem.put("restaurantName", restaurantName);

            cart.add(cartItem);
            session.setAttribute("cart", cart);

            response.put("success", true);
            response.put("cartCount", cart.size());
            response.put("message", "Item added to cart");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to add item: " + e.getMessage());
        }

        return response;
    }

    // Remove item from cart
    @PostMapping("/remove")
    @ResponseBody
    public Map<String, Object> removeFromCart(@RequestParam int index, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

            if (cart != null && index >= 0 && index < cart.size()) {
                cart.remove(index);
                session.setAttribute("cart", cart);
                response.put("success", true);
                response.put("cartCount", cart.size());
            } else {
                response.put("success", false);
                response.put("message", "Invalid item index");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to remove item: " + e.getMessage());
        }

        return response;
    }

    // Get cart
    @GetMapping("/get")
    @ResponseBody
    public Map<String, Object> getCart(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        double total = cart.stream()
                .mapToDouble(item -> ((Number) item.get("price")).doubleValue())
                .sum();

        response.put("cart", cart);
        response.put("cartCount", cart.size());
        response.put("total", total);

        return response;
    }

    // Clear cart
    @PostMapping("/clear")
    @ResponseBody
    public Map<String, Object> clearCart(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        session.removeAttribute("cart");
        response.put("success", true);
        response.put("message", "Cart cleared");
        return response;
    }
}