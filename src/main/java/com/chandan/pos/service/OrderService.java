package com.chandan.pos.service;

import java.util.List;

import com.chandan.pos.domain.OrderStatus;
import com.chandan.pos.domain.PaymentType;
import com.chandan.pos.exceptions.OrderException;
import com.chandan.pos.payload.dto.OrderDto;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto) throws OrderException;

    OrderDto getOrderById(Long id) throws OrderException;

    List<OrderDto> getOrdersByBranch(Long branchId, Long customerId, Long cashierId, PaymentType paymentType, OrderStatus orderStatus);

    List<OrderDto> getOrdersByCashier(Long cashierId);

    List<OrderDto> getTodayOrdersByBranch(Long branchId);

    List<OrderDto> getOrdersByCustomerId(Long customerId);

    List<OrderDto> getTop5RecentOrdersByBranchId(Long branchId);

    void deleteOrder(Long id) throws OrderException;
}