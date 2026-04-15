package com.chandan.pos.payload.dto;

import com.chandan.pos.domain.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeRequestDTO {

    @NotNull(message = "storeId is required")
    private Long storeId;

    @NotNull(message = "planId is required")
    private Long planId;

    // TRIAL or ACTIVE on first subscribe
    private SubscriptionStatus status = SubscriptionStatus.TRIAL;

    private String paymentGateway;
    private String transactionId;
}
