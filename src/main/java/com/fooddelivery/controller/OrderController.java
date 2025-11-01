package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    // View user's orders
    @GetMapping("")
    public String viewOrders(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            List<Order> orders = orderService.getUserOrders(currentUser.getId());
            model.addAttribute("orders", orders);
            model.addAttribute("currentUser", currentUser);
            return "orders";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load orders");
            model.addAttribute("orders", new ArrayList<>());
            model.addAttribute("currentUser", currentUser);
            return "orders";
        }
    }

    // View specific order details
    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderById(id);
            model.addAttribute("order", order);
            model.addAttribute("currentUser", currentUser);
            return "order-details";
        } catch (Exception e) {
            model.addAttribute("error", "Order not found");
            return "redirect:/orders";
        }
    }

    // Checkout page
    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            return "redirect:/home";
        }

        // Calculate totals
        double subtotal = cart.stream()
                .mapToDouble(item -> ((Number) item.get("price")).doubleValue())
                .sum();
        double tax = Math.round(subtotal * 0.05);
        double deliveryFee = 50.0;
        double total = subtotal + tax + deliveryFee;

        model.addAttribute("cart", cart);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("deliveryFee", deliveryFee);
        model.addAttribute("total", total);
        model.addAttribute("currentUser", currentUser);

        return "checkout";
    }

    // Process order
    @PostMapping("/place")
    public String placeOrder(@RequestParam String paymentMethod,
                             @RequestParam String deliveryAddress,
                             @RequestParam String deliveryCity,
                             @RequestParam(required = false) String deliveryName,
                             @RequestParam(required = false) String deliveryPhone,
                             @RequestParam(required = false) String cardNumber,
                             @RequestParam(required = false) String upiId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cart is empty");
            return "redirect:/home";
        }

        try {
            // Calculate totals
            double subtotal = cart.stream()
                    .mapToDouble(item -> ((Number) item.get("price")).doubleValue())
                    .sum();
            double tax = Math.round(subtotal * 0.05);
            double deliveryFee = 50.0;
            double total = subtotal + tax + deliveryFee;

            // Get restaurant ID from first cart item
            Long restaurantId = ((Number) cart.get(0).get("restaurantId")).longValue();

            // Create order
            Order order = new Order();
            order.setUserId(currentUser.getId());
            order.setRestaurantId(restaurantId);
            order.setTotalAmount(total);
            order.setTax(tax);
            order.setDeliveryFee(deliveryFee);
            order.setDeliveryAddress(deliveryAddress + ", " + deliveryCity);
            order.setPaymentMethod(paymentMethod);
            order.setStatus("pending");

            // Create order items
            List<OrderItem> orderItems = new ArrayList<>();
            for (Map<String, Object> item : cart) {
                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItemId(((Number) item.get("id")).longValue());
                orderItem.setName((String) item.get("name"));
                orderItem.setPrice(((Number) item.get("price")).doubleValue());
                orderItem.setQuantity(1);
                orderItems.add(orderItem);
            }
            order.setItems(orderItems);

            // Save order
            Order savedOrder = orderService.createOrder(order);

            // Process payment
            Payment payment = new Payment();
            payment.setOrderId(savedOrder.getId());
            payment.setAmount(total);
            payment.setPaymentMethod(paymentMethod.equals("upi") ? "wallet" : paymentMethod);
            payment.setPaymentStatus("pending");
            paymentService.processPayment(payment);

            // Clear cart
            session.removeAttribute("cart");

            redirectAttributes.addFlashAttribute("success", "Order placed successfully! Order ID: " + savedOrder.getId());
            return "redirect:/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to place order: " + e.getMessage());
            return "redirect:/orders/checkout";
        }
    }

    // Cancel order
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("success", "Order cancelled successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel order: " + e.getMessage());
        }

        return "redirect:/orders";
    }
}