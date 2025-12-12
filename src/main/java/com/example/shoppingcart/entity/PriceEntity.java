package com.example.shoppingcart.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "price")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productType;
    private String clientType;

    @Column(name = "revenue_threshold")
    private Long revenueThreshold;

    private BigDecimal price;

    public PriceEntity(String highEndPhone, String professional, long l, BigDecimal bigDecimal) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Long getRevenueThreshold() {
        return revenueThreshold;
    }

    public void setRevenueThreshold(Long revenueThreshold) {
        this.revenueThreshold = revenueThreshold;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}