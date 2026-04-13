package com.chandan.pos.payload.dto;

import lombok.Data;

@Data
public class OrderItemDto {

    private Long id;

    private Integer quantity;

    private Double price;

    private ProductDTO product;

    private Long productId;

    private Long orderId;
}