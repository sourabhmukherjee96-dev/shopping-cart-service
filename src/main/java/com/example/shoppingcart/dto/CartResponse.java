package com.example.shoppingcart.dto;

import java.math.BigDecimal;

public class CartResponse {

    private BigDecimal totalAmount;

    public CartResponse(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
