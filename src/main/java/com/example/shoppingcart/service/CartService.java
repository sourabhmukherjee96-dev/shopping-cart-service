package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.CartItemDto;
import com.example.shoppingcart.dto.CartRequest;
import com.example.shoppingcart.model.ClientType;
import com.example.shoppingcart.model.ProductType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

@Service
public class CartService {

    private static final BigDecimal TEN_MILLION = BigDecimal.valueOf(10_000_000);


    private static final Map<ProductType, BigDecimal> INDIVIDUAL_PRICES = new EnumMap<>(ProductType.class);

    private static final Map<ProductType, BigDecimal> PRO_HIGH_REVENUE_PRICES = new EnumMap<>(ProductType.class);

    private static final Map<ProductType, BigDecimal> PRO_LOW_REVENUE_PRICES = new EnumMap<>(ProductType.class);

    static {

        INDIVIDUAL_PRICES.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1500));
        INDIVIDUAL_PRICES.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(800));
        INDIVIDUAL_PRICES.put(ProductType.LAPTOP, BigDecimal.valueOf(1200));


        PRO_HIGH_REVENUE_PRICES.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1000));
        PRO_HIGH_REVENUE_PRICES.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(550));
        PRO_HIGH_REVENUE_PRICES.put(ProductType.LAPTOP, BigDecimal.valueOf(900));


        PRO_LOW_REVENUE_PRICES.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1150));
        PRO_LOW_REVENUE_PRICES.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(600));
        PRO_LOW_REVENUE_PRICES.put(ProductType.LAPTOP, BigDecimal.valueOf(1000));
    }

    public BigDecimal calculateTotal(CartRequest request) {
        ClientType type = request.getClient().getClientType();
        Map<ProductType, BigDecimal> priceMap;

        if (type == ClientType.INDIVIDUAL) {
            priceMap = INDIVIDUAL_PRICES;
        } else {
            BigDecimal revenue = BigDecimal.ZERO;
            if (request.getClient().getAnnualRevenue() != null) {
                revenue = BigDecimal.valueOf(request.getClient().getAnnualRevenue());
            }

            if (revenue.compareTo(TEN_MILLION) > 0) {
                priceMap = PRO_HIGH_REVENUE_PRICES;
            } else {
                priceMap = PRO_LOW_REVENUE_PRICES;
            }
        }

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemDto item : request.getItems()) {
            BigDecimal unitPrice = priceMap.get(item.getProductType());
            if (unitPrice == null) {
                throw new IllegalArgumentException("No price defined for product: " + item.getProductType());
            }
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(lineTotal);
        }

        return total;
    }
}
