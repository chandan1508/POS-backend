package com.chandan.pos.mapper;

import com.chandan.pos.modal.ShiftReport;
import com.chandan.pos.payload.dto.ShiftReportDto;

import java.util.stream.Collectors;

public class ShiftReportMapper {

    public static ShiftReportDto toDTO(ShiftReport shiftReport) {
        ShiftReportDto dto = new ShiftReportDto();

        dto.setId(shiftReport.getId());
        dto.setShiftStart(shiftReport.getShiftStart());
        dto.setShiftEnd(shiftReport.getShiftEnd());
        dto.setTotalSales(shiftReport.getTotalSales());
        dto.setTotalRefunds(shiftReport.getTotalRefunds());
        dto.setNetSale(shiftReport.getNetSale());
        dto.setTotalOrders(shiftReport.getTotalOrders());
        dto.setPaymentSummaries(shiftReport.getPaymentSummaries());

        if (shiftReport.getCashier() != null) {
            dto.setCashierId(shiftReport.getCashier().getId());
            dto.setCashier(UserMapper.toDTO(shiftReport.getCashier()));
        }

        if (shiftReport.getBranch() != null) {
            dto.setBranchId(shiftReport.getBranch().getId());
            dto.setBranch(BranchMapper.toDTO(shiftReport.getBranch()));
        }

        if (shiftReport.getTopSellingProducts() != null) {
            dto.setTopSellingProducts(
                shiftReport.getTopSellingProducts()
                    .stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList())
            );
        }

        if (shiftReport.getRecentOrders() != null) {
            dto.setRecentOrders(
                shiftReport.getRecentOrders()
                    .stream()
                    .map(OrderMapper::toDTO)
                    .collect(Collectors.toList())
            );
        }

        if (shiftReport.getRefunds() != null) {
            dto.setRefunds(
                shiftReport.getRefunds()
                    .stream()
                    .map(RefundMapper::toDTO)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }
}