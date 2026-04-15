package com.chandan.pos.payload.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.chandan.pos.domain.BillingCycle;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private BillingCycle billingCycle;
    private Integer maxBranches;
    private Integer maxUsers;
    private Integer maxProducts;
    private Boolean enableAdvancedReports;
    private Boolean enableInventory;
    private Boolean enableIntegrations;
    private Boolean enableEcommerce;
    private Boolean enableInvoiceBranding;
    private Boolean prioritySupport;
    private Boolean enableMultiLocation;
    private List<String> extraFeatures;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
