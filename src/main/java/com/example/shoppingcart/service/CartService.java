package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.CartItemDto;
import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.entity.PriceEntity;
import com.example.shoppingcart.exception.ResourceNotFoundException;
import com.example.shoppingcart.model.ClientType;
import com.example.shoppingcart.model.ProductType;
import com.example.shoppingcart.repository.PriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

    private final PriceRepository priceRepository;

    public CartService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotal(CartRequest request) {
        // Basic null-safety: controller is expected to validate, but service defends as well.
        Objects.requireNonNull(request, "CartRequest must not be null");
        Objects.requireNonNull(request.getClient(), "Client information is required");
        Objects.requireNonNull(request.getItems(), "Cart items are required");

        // Extract client type
        ClientType clientType = Optional.ofNullable(request.getClient())
                .map(c -> c.getClientType())
                .orElseThrow(() -> new IllegalArgumentException("ClientType is required"));

        // Extract revenue (default 0 if not provided)
        long revenue = Optional.ofNullable(request.getClient())
                .map(c -> c.getAnnualRevenue())
                .map(Double::longValue)
                .orElse(0L);

        // Stream through items: validate, lookup price, compute line total, then sum
        return request.getItems().stream()
                .peek(this::validateItem) // validate each item (throws IllegalArgumentException if invalid)
                .map(item -> calculateLineTotal(item, clientType, revenue))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Validate that the cart item contains a product and a positive quantity.
     */
    private void validateItem(CartItemDto item) {
        if (item == null) {
            throw new IllegalArgumentException("Cart item must not be null");
        }
        if (item.getProductType() == null) {
            throw new IllegalArgumentException("Product type is required for each cart item");
        }
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive integer");
        }
    }

    /**
     * For a single cart item, find the unit price from DB and return unitPrice * quantity.
     * Throws ResourceNotFoundException if no DB price row is found.
     */
    private BigDecimal calculateLineTotal(CartItemDto item, ClientType clientType, long revenue) {
        ProductType product = item.getProductType();

        Optional<PriceEntity> optPrice = priceRepository.findBestPrice(
                product.name(),
                clientType.name(),
                revenue
        );

        BigDecimal unitPrice = optPrice
                .map(PriceEntity::getPrice)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("No price found for product=%s, clientType=%s, revenue=%d",
                                product.name(), clientType.name(), revenue)));

        return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
