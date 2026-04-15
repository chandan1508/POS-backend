package com.chandan.pos.mapper;

import org.springframework.stereotype.Component;

import com.chandan.pos.modal.SubscriptionPlan;
import com.chandan.pos.payload.dto.SubscriptionPlanRequestDTO;
import com.chandan.pos.payload.dto.SubscriptionPlanResponseDTO;

@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlan toEntity(SubscriptionPlanRequestDTO dto) {
        return SubscriptionPlan.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .billingCycle(dto.getBillingCycle())
                .maxBranches(dto.getMaxBranches())
                .maxUsers(dto.getMaxUsers())
                .maxProducts(dto.getMaxProducts())
                .enableAdvancedReports(Boolean.TRUE.equals(dto.getEnableAdvancedReports()))
                .enableInventory(Boolean.TRUE.equals(dto.getEnableInventory()))
                .enableIntegrations(Boolean.TRUE.equals(dto.getEnableIntegrations()))
                .enableEcommerce(Boolean.TRUE.equals(dto.getEnableEcommerce()))
                .enableInvoiceBranding(Boolean.TRUE.equals(dto.getEnableInvoiceBranding()))
                .prioritySupport(Boolean.TRUE.equals(dto.getPrioritySupport()))
                .enableMultiLocation(Boolean.TRUE.equals(dto.getEnableMultiLocation()))
                .extraFeatures(dto.getExtraFeatures())
                .build();
    }

    public SubscriptionPlanResponseDTO toResponseDTO(SubscriptionPlan entity) {
        return SubscriptionPlanResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .billingCycle(entity.getBillingCycle())
                .maxBranches(entity.getMaxBranches())
                .maxUsers(entity.getMaxUsers())
                .maxProducts(entity.getMaxProducts())
                .enableAdvancedReports(entity.getEnableAdvancedReports())
                .enableInventory(entity.getEnableInventory())
                .enableIntegrations(entity.getEnableIntegrations())
                .enableEcommerce(entity.getEnableEcommerce())
                .enableInvoiceBranding(entity.getEnableInvoiceBranding())
                .prioritySupport(entity.getPrioritySupport())
                .enableMultiLocation(entity.getEnableMultiLocation())
                .extraFeatures(entity.getExtraFeatures())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(SubscriptionPlanRequestDTO dto, SubscriptionPlan entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setBillingCycle(dto.getBillingCycle());
        entity.setMaxBranches(dto.getMaxBranches());
        entity.setMaxUsers(dto.getMaxUsers());
        entity.setMaxProducts(dto.getMaxProducts());
        entity.setEnableAdvancedReports(Boolean.TRUE.equals(dto.getEnableAdvancedReports()));
        entity.setEnableInventory(Boolean.TRUE.equals(dto.getEnableInventory()));
        entity.setEnableIntegrations(Boolean.TRUE.equals(dto.getEnableIntegrations()));
        entity.setEnableEcommerce(Boolean.TRUE.equals(dto.getEnableEcommerce()));
        entity.setEnableInvoiceBranding(Boolean.TRUE.equals(dto.getEnableInvoiceBranding()));
        entity.setPrioritySupport(Boolean.TRUE.equals(dto.getPrioritySupport()));
        entity.setEnableMultiLocation(Boolean.TRUE.equals(dto.getEnableMultiLocation()));
        entity.setExtraFeatures(dto.getExtraFeatures());
    }
}
