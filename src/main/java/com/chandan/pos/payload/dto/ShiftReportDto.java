package com.chandan.pos.payload.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.chandan.pos.modal.PaymentSummary;

import lombok.Data;

@Data
public class ShiftReportDto {

    private Long id;

    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;

    private Double totalSales;
    private Double totalRefunds;
    private Double netSale;
    private Double totalOrders;

    private UserDto cashier;
    private Long cashierId;

    private BranchDTO branch;
    private Long branchId;

    private List<PaymentSummary> paymentSummaries;

    private List<ProductDTO> topSellingProducts;

    private List<OrderDto> recentOrders;

    private List<RefundDto> refunds;
}