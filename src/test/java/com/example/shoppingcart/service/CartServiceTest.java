package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.CartItemDto;
import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.dto.ClientDto;
import com.example.shoppingcart.entity.PriceEntity;
import com.example.shoppingcart.model.ClientType;
import com.example.shoppingcart.model.ProductType;
import com.example.shoppingcart.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

class CartServiceTest {

    private PriceRepository priceRepository;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        priceRepository = Mockito.mock(PriceRepository.class);
        cartService = new CartService(priceRepository);
    }

    @Test
    void calculateTotal_proHighRevenue_usesRepoPrices() {
        ClientDto client = new ClientDto();
        client.setClientType(ClientType.PROFESSIONAL);
        client.setAnnualRevenue(11_000_000.0);

        CartItemDto item1 = new CartItemDto();
        item1.setProductType(ProductType.HIGH_END_PHONE);
        item1.setQuantity(2);

        CartItemDto item2 = new CartItemDto();
        item2.setProductType(ProductType.LAPTOP);
        item2.setQuantity(3);

        CartRequest request = new CartRequest();
        request.setClient(client);
        request.setItems(List.of(item1, item2));

        // Mock repository to return prices matching the high-revenue tier
        PriceEntity phonePrice = new PriceEntity("HIGH_END_PHONE", "PROFESSIONAL", 10000001L, BigDecimal.valueOf(1000));
        PriceEntity laptopPrice = new PriceEntity("LAPTOP", "PROFESSIONAL", 10000001L, BigDecimal.valueOf(900));

        Mockito.when(priceRepository.findBestPrice(eq("HIGH_END_PHONE"), eq("PROFESSIONAL"), anyLong()))
                .thenReturn(Optional.of(phonePrice));
        Mockito.when(priceRepository.findBestPrice(eq("LAPTOP"), eq("PROFESSIONAL"), anyLong()))
                .thenReturn(Optional.of(laptopPrice));

        BigDecimal total = cartService.calculateTotal(request);

        // 2 * 1000 + 3 * 900 = 4700
        assertEquals(BigDecimal.valueOf(4700), total);
    }

    @Test
    void calculateTotal_individual_usesRepoPrices() {
        ClientDto client = new ClientDto();
        client.setClientType(ClientType.INDIVIDUAL);

        CartItemDto item = new CartItemDto();
        item.setProductType(ProductType.MID_RANGE_PHONE);
        item.setQuantity(4);

        CartRequest request = new CartRequest();
        request.setClient(client);
        request.setItems(List.of(item));

        PriceEntity midPrice = new PriceEntity("MID_RANGE_PHONE", "INDIVIDUAL", 0L, BigDecimal.valueOf(800));

        Mockito.when(priceRepository.findBestPrice(eq("MID_RANGE_PHONE"), eq("INDIVIDUAL"), anyLong()))
                .thenReturn(Optional.of(midPrice));

        BigDecimal total = cartService.calculateTotal(request);

        // 4 * 800 = 3200
        assertEquals(BigDecimal.valueOf(3200), total);
    }
}
