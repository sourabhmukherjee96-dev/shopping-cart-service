package com.example.shoppingcart.controller;

import com.example.shoppingcart.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void calculateTotal_returnsTotalAmount() throws Exception {
        when(cartService.calculateTotal(any())).thenReturn(BigDecimal.valueOf(3500));

        String requestJson = """
                {
                  "client": {
                    "clientType": "INDIVIDUAL",
                    "clientId": "C123",
                    "firstName": "John",
                    "lastName": "Doe"
                  },
                  "items": [
                    { "productType": "HIGH_END_PHONE", "quantity": 1 },
                    { "productType": "MID_RANGE_PHONE", "quantity": 1 },
                    { "productType": "LAPTOP", "quantity": 1 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/cart/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(3500));
    }
}
