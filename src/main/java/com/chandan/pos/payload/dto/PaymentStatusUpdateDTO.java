package com.chandan.pos.payload.dto;

import com.chandan.pos.domain.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusUpdateDTO {

    @NotNull(message = "paymentStatus is required")
    private PaymentStatus paymentStatus;

    private String transactionId;
    private String paymentGateway;
}
