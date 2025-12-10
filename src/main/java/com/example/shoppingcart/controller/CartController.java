package com.example.shoppingcart.controller;

import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.dto.CartResponse;
import com.example.shoppingcart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/")
    public Map<String, String> status() {
        return Map.of("message", "service is up");
    }


    @PostMapping("/total")
    public ResponseEntity<CartResponse> calculateTotal(@Valid @RequestBody CartRequest request) {
        BigDecimal total = cartService.calculateTotal(request);
        return ResponseEntity.ok(new CartResponse(total));
    }
}
