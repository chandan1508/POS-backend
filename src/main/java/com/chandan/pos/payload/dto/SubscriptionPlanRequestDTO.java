package com.chandan.pos.payload.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

import com.chandan.pos.domain.BillingCycle;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanRequestDTO {

    @NotBlank(message = "Plan name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @NotNull(message = "Billing cycle is required")
    private BillingCycle billingCycle;

    @Min(value = 1, message = "maxBranches must be at least 1")
    private Integer maxBranches;

    @Min(value = 1, message = "maxUsers must be at least 1")
    private Integer maxUsers;

    @Min(value = 1, message = "maxProducts must be at least 1")
    private Integer maxProducts;

    private Boolean enableAdvancedReports = false;
    private Boolean enableInventory = false;
    private Boolean enableIntegrations = false;
    private Boolean enableEcommerce = false;
    private Boolean enableInvoiceBranding = false;
    private Boolean prioritySupport = false;
    private Boolean enableMultiLocation = false;

    private List<String> extraFeatures;
}
