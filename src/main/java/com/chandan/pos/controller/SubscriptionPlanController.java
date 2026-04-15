package com.chandan.pos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chandan.pos.payload.dto.SubscriptionPlanRequestDTO;
import com.chandan.pos.payload.dto.SubscriptionPlanResponseDTO;
import com.chandan.pos.service.SubscriptionPlanService;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    /**
     * POST /api/super-admin/subscription-plans
     * Create a new subscription plan.
     */
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponseDTO> createPlan(
            @Valid @RequestBody SubscriptionPlanRequestDTO requestDTO) {
        SubscriptionPlanResponseDTO created = subscriptionPlanService.createPlan(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/super-admin/subscription-plans/{id}
     * Update an existing subscription plan.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDTO> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanRequestDTO requestDTO) {
        SubscriptionPlanResponseDTO updated = subscriptionPlanService.updatePlan(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /api/super-admin/subscription-plans
     * Retrieve all subscription plans.
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionPlanResponseDTO>> getAllPlans() {
        return ResponseEntity.ok(subscriptionPlanService.getAllPlans());
    }

    /**
     * GET /api/super-admin/subscription-plans/{id}
     * Fetch a specific subscription plan by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponseDTO> getPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionPlanService.getPlanById(id));
    }

    /**
     * DELETE /api/super-admin/subscription-plans/{id}
     * Delete a subscription plan by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        subscriptionPlanService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
