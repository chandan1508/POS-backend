package com.chandan.pos.service;

import java.util.List;

import com.chandan.pos.payload.dto.SubscriptionPlanRequestDTO;
import com.chandan.pos.payload.dto.SubscriptionPlanResponseDTO;

public interface SubscriptionPlanService {

    SubscriptionPlanResponseDTO createPlan(SubscriptionPlanRequestDTO requestDTO);

    SubscriptionPlanResponseDTO updatePlan(Long id, SubscriptionPlanRequestDTO requestDTO);

    List<SubscriptionPlanResponseDTO> getAllPlans();

    SubscriptionPlanResponseDTO getPlanById(Long id);

    void deletePlan(Long id);
}
