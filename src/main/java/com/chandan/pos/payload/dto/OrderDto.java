package com.chandan.pos.payload.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.chandan.pos.domain.PaymentType;
import com.chandan.pos.modal.Customer;

import lombok.Data;

@Data
public class OrderDto {

    private Long id;

    private double totalAmount;

    private LocalDateTime createdAt;

 
    private Long branchId;
    private Long customerId;

    private BranchDTO branch;

    private UserDto cashier;

    private Customer customer;
 
    private PaymentType paymentType;


    private List<OrderItemDto> items;
}