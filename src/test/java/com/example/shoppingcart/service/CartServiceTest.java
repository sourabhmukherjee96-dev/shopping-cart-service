package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.CartItemDto;
import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.dto.ClientDto;
import com.example.shoppingcart.model.ClientType;
import com.example.shoppingcart.model.ProductType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartServiceTest {

    private final CartService cartService = new CartService();

    private CartRequest buildIndividualRequest() {
        ClientDto client = new ClientDto();
        client.setClientType(ClientType.INDIVIDUAL);
        client.setClientId("IND-1");
        client.setFirstName("Alice");
        client.setLastName("Smith");

        CartItemDto item1 = new CartItemDto();
        item1.setProductType(ProductType.HIGH_END_PHONE);
        item1.setQuantity(1);

        CartItemDto item2 = new CartItemDto();
        item2.setProductType(ProductType.MID_RANGE_PHONE);
        item2.setQuantity(1);

        CartItemDto item3 = new CartItemDto();
        item3.setProductType(ProductType.LAPTOP);
        item3.setQuantity(1);

        CartRequest request = new CartRequest();
        request.setClient(client);
        request.setItems(List.of(item1, item2, item3));
        return request;
    }

    private CartRequest buildProHighRevenueRequest() {
        ClientDto client = new ClientDto();
        client.setClientType(ClientType.PROFESSIONAL);
        client.setClientId("PRO-1");
        client.setCompanyName("Big Corp");
        client.setAnnualRevenue(11_000_000.0); // > 10M

        CartItemDto highEnd = new CartItemDto();
        highEnd.setProductType(ProductType.HIGH_END_PHONE);
        highEnd.setQuantity(2);

        CartItemDto laptop = new CartItemDto();
        laptop.setProductType(ProductType.LAPTOP);
        laptop.setQuantity(3);

        CartRequest request = new CartRequest();
        request.setClient(client);
        request.setItems(List.of(highEnd, laptop));
        return request;
    }

    private CartRequest buildProLowRevenueRequest() {
        ClientDto client = new ClientDto();
        client.setClientType(ClientType.PROFESSIONAL);
        client.setClientId("PRO-2");
        client.setCompanyName("Small Biz");
        client.setAnnualRevenue(5_000_000.0); // <= 10M

        CartItemDto midRange = new CartItemDto();
        midRange.setProductType(ProductType.MID_RANGE_PHONE);
        midRange.setQuantity(4);

        CartItemDto laptop = new CartItemDto();
        laptop.setProductType(ProductType.LAPTOP);
        laptop.setQuantity(1);

        CartRequest request = new CartRequest();
        request.setClient(client);
        request.setItems(List.of(midRange, laptop));
        return request;
    }

    @Test
    void calculateTotal_forIndividualClient_correctTotal() {
        CartRequest request = buildIndividualRequest();

        BigDecimal total = cartService.calculateTotal(request);

        // 1 * 1500 + 1 * 800 + 1 * 1200 = 1500 + 800 + 1200 = 3500
        assertEquals(BigDecimal.valueOf(3500), total);
    }

    @Test
    void calculateTotal_forProfessionalHighRevenue_correctTotal() {
        CartRequest request = buildProHighRevenueRequest();

        BigDecimal total = cartService.calculateTotal(request);

        // High revenue professional:
        // high-end: 1000, laptop: 900
        // 2 * 1000 + 3 * 900 = 2000 + 2700 = 4700
        assertEquals(BigDecimal.valueOf(4700), total);
    }

    @Test
    void calculateTotal_forProfessionalLowRevenue_correctTotal() {
        CartRequest request = buildProLowRevenueRequest();

        BigDecimal total = cartService.calculateTotal(request);

        // Low revenue professional:
        // mid-range: 600, laptop: 1000
        // 4 * 600 + 1 * 1000 = 2400 + 1000 = 3400
        assertEquals(BigDecimal.valueOf(3400), total);
    }
}
