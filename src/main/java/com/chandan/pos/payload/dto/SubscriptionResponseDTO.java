package com.chandan.pos.payload.dto;

import com.chandan.pos.domain.PaymentStatus;
import com.chandan.pos.domain.SubscriptionStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDTO {

    private Long id;

    // Store summary
    private Long storeId;
    private String storeBrand;

    // Plan summary
    private Long planId;
    private String planName;
    private Double planPrice;

    private LocalDate startDate;
    private LocalDate endDate;
    private SubscriptionStatus status;
    private String paymentGateway;
    private String transactionId;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}