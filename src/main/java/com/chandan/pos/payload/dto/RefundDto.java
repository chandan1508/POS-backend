package com.chandan.pos.payload.dto;

import java.time.LocalDateTime;

import com.chandan.pos.domain.PaymentType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefundDto {

    private Long id;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private OrderDto order;

    private String reason;

    @NotNull(message = "Refund amount is required")
    private Double amount;

    private Long shiftReportId;

    private UserDto cashier;
    private Long cashierId;

    private BranchDTO branch;
    private Long branchId;

    private LocalDateTime createdAt;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;
}