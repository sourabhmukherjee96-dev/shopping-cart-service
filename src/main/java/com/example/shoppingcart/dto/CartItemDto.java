package com.example.shoppingcart.dto;

import com.example.shoppingcart.model.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemDto {

    @NotNull
    private ProductType productType;

    @Min(1)
    private int quantity;

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
