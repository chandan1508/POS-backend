package com.chandan.pos.payload.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDTO {

    private Long id;

    private BranchDTO branch;

    private ProductDTO product;

    private Long branchId;

    private Long productId;

    private Integer quantity;

    private LocalDateTime lastUpdated;
}