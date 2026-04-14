package com.chandan.pos.mapper;

import com.chandan.pos.modal.Refund;
import com.chandan.pos.payload.dto.RefundDto;

public class RefundMapper {

    public static RefundDto toDTO(Refund refund) {
        RefundDto dto = new RefundDto();

        dto.setId(refund.getId());
        dto.setReason(refund.getReason());
        dto.setAmount(refund.getAmount());
        dto.setCreatedAt(refund.getCreatedAt());
        dto.setPaymentType(refund.getPaymentType());

        if (refund.getOrder() != null) {
            dto.setOrderId(refund.getOrder().getId());
            dto.setOrder(OrderMapper.toDTO(refund.getOrder()));
        }

        if (refund.getCashier() != null) {
            dto.setCashierId(refund.getCashier().getId());
            dto.setCashier(UserMapper.toDTO(refund.getCashier()));
        }

        if (refund.getBranch() != null) {
            dto.setBranchId(refund.getBranch().getId());
            dto.setBranch(BranchMapper.toDTO(refund.getBranch()));
        }

        if (refund.getShiftReport() != null) {
            dto.setShiftReportId(refund.getShiftReport().getId());
        }

        return dto;
    }
}