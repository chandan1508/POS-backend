package com.chandan.pos.service.impl;

import com.chandan.pos.domain.PaymentStatus;
import com.chandan.pos.domain.SubscriptionStatus;
import com.chandan.pos.mapper.SubscriptionMapper;
import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.Subscription;
import com.chandan.pos.modal.SubscriptionPlan;
import com.chandan.pos.payload.dto.PaymentStatusUpdateDTO;
import com.chandan.pos.payload.dto.SubscribeRequestDTO;
import com.chandan.pos.payload.dto.SubscriptionResponseDTO;
import com.chandan.pos.payload.dto.UpgradeRequestDTO;
import com.chandan.pos.repository.StoreRepository;
import com.chandan.pos.repository.SubscriptionPlanRepository;
import com.chandan.pos.repository.SubscriptionRepository;
import com.chandan.pos.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final StoreRepository storeRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionMapper subscriptionMapper;

    // ─── Subscribe ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public SubscriptionResponseDTO subscribe(SubscribeRequestDTO request) {
        Store store = findStore(request.getStoreId());
        SubscriptionPlan plan = findPlan(request.getPlanId());

        // Cancel any existing active/trial subscription
        List<SubscriptionStatus> active = List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.TRIAL);
        subscriptionRepository
                .findTopByStoreIdAndStatusInOrderByCreatedAtDesc(store.getId(), active)
                .ifPresent(existing -> {
                    existing.setStatus(SubscriptionStatus.CANCELLED);
                    subscriptionRepository.save(existing);
                });

        SubscriptionStatus requestedStatus = request.getStatus() != null
                ? request.getStatus()
                : SubscriptionStatus.TRIAL;

        LocalDate start = LocalDate.now();
        LocalDate end = resolveEndDate(start, plan, requestedStatus);

        Subscription subscription = Subscription.builder()
                .store(store)
                .plan(plan)
                .startDate(start)
                .endDate(end)
                .status(requestedStatus)
                .paymentGateway(request.getPaymentGateway())
                .transactionId(request.getTransactionId())
                .paymentStatus(requestedStatus == SubscriptionStatus.TRIAL
                        ? PaymentStatus.PENDING
                        : PaymentStatus.PAID)
                .build();

        return subscriptionMapper.toResponseDTO(subscriptionRepository.save(subscription));
    }

    // ─── Upgrade ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public SubscriptionResponseDTO upgrade(UpgradeRequestDTO request) {
        Store store = findStore(request.getStoreId());
        SubscriptionPlan newPlan = findPlan(request.getNewPlanId());

        List<SubscriptionStatus> active = List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.TRIAL);
        Subscription current = subscriptionRepository
                .findTopByStoreIdAndStatusInOrderByCreatedAtDesc(store.getId(), active)
                .orElseThrow(() -> new IllegalStateException(
                        "No active or trial subscription found for store id: " + store.getId()));

        // Cancel current and create new subscription with the new plan
        current.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(current);

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusMonths(1); // Default 1 month; adjust by billing cycle as needed

        Subscription upgraded = Subscription.builder()
                .store(store)
                .plan(newPlan)
                .startDate(start)
                .endDate(end)
                .status(SubscriptionStatus.ACTIVE)
                .paymentGateway(request.getPaymentGateway())
                .transactionId(request.getTransactionId())
                .paymentStatus(PaymentStatus.PAID)
                .build();

        return subscriptionMapper.toResponseDTO(subscriptionRepository.save(upgraded));
    }

    // ─── Activate ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public SubscriptionResponseDTO activate(Long subscriptionId) {
        Subscription subscription = findSubscription(subscriptionId);

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot activate a cancelled subscription.");
        }

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setPaymentStatus(PaymentStatus.PAID);

        // Reset dates from today on activation
        LocalDate start = LocalDate.now();
        subscription.setStartDate(start);
        subscription.setEndDate(resolveEndDate(start, subscription.getPlan(), SubscriptionStatus.ACTIVE));

        return subscriptionMapper.toResponseDTO(subscriptionRepository.save(subscription));
    }

    // ─── Cancel ──────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public SubscriptionResponseDTO cancel(Long subscriptionId) {
        Subscription subscription = findSubscription(subscriptionId);

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription is already cancelled.");
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        return subscriptionMapper.toResponseDTO(subscriptionRepository.save(subscription));
    }

    // ─── Update Payment Status ────────────────────────────────────────────────

    @Override
    @Transactional
    public SubscriptionResponseDTO updatePaymentStatus(Long subscriptionId, PaymentStatusUpdateDTO dto) {
        Subscription subscription = findSubscription(subscriptionId);

        subscription.setPaymentStatus(dto.getPaymentStatus());
        if (dto.getTransactionId() != null) {
            subscription.setTransactionId(dto.getTransactionId());
        }
        if (dto.getPaymentGateway() != null) {
            subscription.setPaymentGateway(dto.getPaymentGateway());
        }

        return subscriptionMapper.toResponseDTO(subscriptionRepository.save(subscription));
    }

    // ─── Get By Store ─────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDTO> getByStore(Long storeId, SubscriptionStatus status) {
        List<Subscription> results = (status != null)
                ? subscriptionRepository.findByStoreIdAndStatus(storeId, status)
                : subscriptionRepository.findByStoreId(storeId);

        return results.stream()
                .map(subscriptionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Admin: Get All ───────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDTO> getAllAdmin(SubscriptionStatus status) {
        List<Subscription> results = (status != null)
                ? subscriptionRepository.findByStatus(status)
                : subscriptionRepository.findAll();

        return results.stream()
                .map(subscriptionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Admin: Expiring ──────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDTO> getExpiring(int days) {
        LocalDate now = LocalDate.now();
        LocalDate threshold = now.plusDays(days);

        return subscriptionRepository
                .findByStatusAndEndDateBetween(SubscriptionStatus.ACTIVE, now, threshold)
                .stream()
                .map(subscriptionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ─── Admin: Count by Status ───────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Map<SubscriptionStatus, Long> countByStatus(SubscriptionStatus status) {
        if (status != null) {
            return Map.of(status, subscriptionRepository.countByStatus(status));
        }

        List<Object[]> rows = subscriptionRepository.countGroupedByStatus();
        Map<SubscriptionStatus, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            result.put((SubscriptionStatus) row[0], (Long) row[1]);
        }
        return result;
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id: " + storeId));
    }

    private SubscriptionPlan findPlan(Long planId) {
        return subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan not found with id: " + planId));
    }

    private Subscription findSubscription(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found with id: " + subscriptionId));
    }

    /**
     * Determine end date based on the plan's billing cycle.
     * TRIAL always gets 14 days; MONTHLY = +1 month; YEARLY = +1 year.
     */
    private LocalDate resolveEndDate(LocalDate start, SubscriptionPlan plan, SubscriptionStatus status) {
        if (status == SubscriptionStatus.TRIAL) {
            return start.plusDays(14);
        }
        return switch (plan.getBillingCycle()) {
            case YEARLY -> start.plusYears(1);
            default -> start.plusMonths(1);
        };
    }
}
