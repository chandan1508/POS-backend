package com.chandan.pos.payload.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpgradeRequestDTO {

    @NotNull(message = "storeId is required")
    private Long storeId;

    @NotNull(message = "newPlanId is required")
    private Long newPlanId;

    private String paymentGateway;
    private String transactionId;
}
