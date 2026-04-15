package com.chandan.pos.controller;



import com.chandan.pos.domain.PaymentStatus;
import com.chandan.pos.domain.SubscriptionStatus;
import com.chandan.pos.payload.dto.PaymentStatusUpdateDTO;
import com.chandan.pos.payload.dto.SubscribeRequestDTO;
import com.chandan.pos.payload.dto.SubscriptionResponseDTO;
import com.chandan.pos.payload.dto.UpgradeRequestDTO;
import com.chandan.pos.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * POST /api/subscriptions/subscribe
     * Create a new subscription for a store (trial or first-time plan).
     */
    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionResponseDTO> subscribe(
            @Valid @RequestBody SubscribeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.subscribe(request));
    }

    /**
     * POST /api/subscriptions/upgrade
     * Upgrade a store's existing subscription to a new plan.
     */
    @PostMapping("/upgrade")
    public ResponseEntity<SubscriptionResponseDTO> upgrade(
            @Valid @RequestBody UpgradeRequestDTO request) {
        return ResponseEntity.ok(subscriptionService.upgrade(request));
    }

    /**
     * PUT /api/subscriptions/{subscriptionId}/activate
     * Admin activates a subscription (usually after payment confirmation).
     */
    @PutMapping("/{subscriptionId}/activate")
    public ResponseEntity<SubscriptionResponseDTO> activate(
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.activate(subscriptionId));
    }

    /**
     * PUT /api/subscriptions/{subscriptionId}/cancel
     * Admin cancels a subscription manually.
     */
    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponseDTO> cancel(
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.cancel(subscriptionId));
    }

    /**
     * PUT /api/subscriptions/{subscriptionId}/payment-status
     * Update a subscription's payment status (manual override).
     */
    @PutMapping("/{subscriptionId}/payment-status")
    public ResponseEntity<SubscriptionResponseDTO> updatePaymentStatus(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody PaymentStatusUpdateDTO dto) {
        return ResponseEntity.ok(subscriptionService.updatePaymentStatus(subscriptionId, dto));
    }

    /**
     * GET /api/subscriptions/store/{storeId}?status=ACTIVE
     * Get all subscriptions for a specific store, optionally filtered by status.
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<SubscriptionResponseDTO>> getByStore(
            @PathVariable Long storeId,
            @RequestParam(required = false) SubscriptionStatus status) {
        return ResponseEntity.ok(subscriptionService.getByStore(storeId, status));
    }

    /**
     * GET /api/subscriptions/admin?status=ACTIVE
     * Admin: Get all subscriptions, with optional status filter.
     */
    @GetMapping("/admin")
    public ResponseEntity<List<SubscriptionResponseDTO>> getAllAdmin(
            @RequestParam(required = false) SubscriptionStatus status) {
        return ResponseEntity.ok(subscriptionService.getAllAdmin(status));
    }

    /**
     * GET /api/subscriptions/admin/expiring?days=7
     * Get subscriptions expiring within the next N days.
     */
    @GetMapping("/admin/expiring")
    public ResponseEntity<List<SubscriptionResponseDTO>> getExpiring(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(subscriptionService.getExpiring(days));
    }

    /**
     * GET /api/subscriptions/admin/count?status=ACTIVE
     * Count subscriptions grouped by status (or filtered to one status).
     */
    @GetMapping("/admin/count")
    public ResponseEntity<Map<SubscriptionStatus, Long>> countByStatus(
            @RequestParam(required = false) SubscriptionStatus status) {
        return ResponseEntity.ok(subscriptionService.countByStatus(status));
    }
}
