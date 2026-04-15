package com.chandan.pos.service;



import com.chandan.pos.domain.PaymentStatus;
import com.chandan.pos.domain.SubscriptionStatus;
import com.chandan.pos.payload.dto.PaymentStatusUpdateDTO;
import com.chandan.pos.payload.dto.SubscribeRequestDTO;
import com.chandan.pos.payload.dto.SubscriptionResponseDTO;
import com.chandan.pos.payload.dto.UpgradeRequestDTO;

import java.util.List;
import java.util.Map;

public interface SubscriptionService {

    /** Create a new subscription (trial or first-time active). */
    SubscriptionResponseDTO subscribe(SubscribeRequestDTO request);

    /** Upgrade an existing store subscription to a new plan. */
    SubscriptionResponseDTO upgrade(UpgradeRequestDTO request);

    /** Admin activates a subscription after confirming payment. */
    SubscriptionResponseDTO activate(Long subscriptionId);

    /** Admin cancels a subscription manually. */
    SubscriptionResponseDTO cancel(Long subscriptionId);

    /** Manual override of payment status on a subscription. */
    SubscriptionResponseDTO updatePaymentStatus(Long subscriptionId, PaymentStatusUpdateDTO dto);

    /** All subscriptions for a store, optionally filtered by status. */
    List<SubscriptionResponseDTO> getByStore(Long storeId, SubscriptionStatus status);

    /** All subscriptions in the system, optionally filtered by status. */
    List<SubscriptionResponseDTO> getAllAdmin(SubscriptionStatus status);

    /** Subscriptions expiring within the next N days. */
    List<SubscriptionResponseDTO> getExpiring(int days);

    /** Count of subscriptions grouped by status. */
    Map<SubscriptionStatus, Long> countByStatus(SubscriptionStatus status);
}
