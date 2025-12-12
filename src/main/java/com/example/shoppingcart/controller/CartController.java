package com.example.shoppingcart.controller;

import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.dto.CartResponse;
import com.example.shoppingcart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = "/total", headers = "X-API-VERSION=1")
    public ResponseEntity<CartResponse> calculateTotalV1(@Valid @RequestBody CartRequest request) {
        BigDecimal total = cartService.calculateTotal(request);
        return ResponseEntity.ok(new CartResponse(total));
    }
}
