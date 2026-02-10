package com.smartcommerce.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int categoryId;
    private String categoryName;
    private int quantityAvailable;
    private Timestamp createdAt;
}