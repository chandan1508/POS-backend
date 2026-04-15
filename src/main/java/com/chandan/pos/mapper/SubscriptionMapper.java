package com.chandan.pos.mapper;

import com.chandan.pos.modal.Subscription;
import com.chandan.pos.payload.dto.SubscriptionResponseDTO;

import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public SubscriptionResponseDTO toResponseDTO(Subscription subscription) {
        return SubscriptionResponseDTO.builder()
                .id(subscription.getId())
                .storeId(subscription.getStore().getId())
                .storeBrand(subscription.getStore().getBrand())
                .planId(subscription.getPlan().getId())
                .planName(subscription.getPlan().getName())
                .planPrice(subscription.getPlan().getPrice())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .paymentGateway(subscription.getPaymentGateway())
                .transactionId(subscription.getTransactionId())
                .paymentStatus(subscription.getPaymentStatus())
                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())
                .build();
    }
}
