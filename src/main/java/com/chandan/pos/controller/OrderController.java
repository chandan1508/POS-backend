package com.chandan.pos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chandan.pos.domain.OrderStatus;
import com.chandan.pos.domain.PaymentType;
import com.chandan.pos.exceptions.OrderException;
import com.chandan.pos.payload.dto.OrderDto;
import com.chandan.pos.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // POST /api/orders — ROLE_CASHIER
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @Valid @RequestBody OrderDto orderDto
    ) throws OrderException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderDto));
    }

    // GET /api/orders/{id} — Public
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable Long id
    ) throws OrderException {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // GET /api/orders/branch/{branchId}?customerId=&cashierId=&paymentType=&orderStatus= — Public
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<OrderDto>> getOrdersByBranch(
            @PathVariable  Long        branchId,
            @RequestParam(required = false) Long        customerId,
            @RequestParam(required = false) Long        cashierId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) OrderStatus orderStatus
    ) {
        return ResponseEntity.ok(
                orderService.getOrdersByBranch(branchId, customerId, cashierId, paymentType, orderStatus)
        );
    }

    // GET /api/orders/cashier/{cashierId} — Public
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCashier(
            @PathVariable Long cashierId
    ) {
        return ResponseEntity.ok(orderService.getOrdersByCashier(cashierId));
    }

    // GET /api/orders/today/branch/{branchId} — Public
    @GetMapping("/today/branch/{branchId}")
    public ResponseEntity<List<OrderDto>> getTodayOrdersByBranch(
            @PathVariable Long branchId
    ) {
        return ResponseEntity.ok(orderService.getTodayOrdersByBranch(branchId));
    }

    // GET /api/orders/customer/{customerId} — Public
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomer(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    // GET /api/orders/recent/{branchId} — ROLE_BRANCH_MANAGER, ROLE_BRANCH_ADMIN
    @GetMapping("/recent/{branchId}")
    public ResponseEntity<List<OrderDto>> getRecentOrdersByBranch(
            @PathVariable Long branchId
    ) {
        return ResponseEntity.ok(orderService.getTop5RecentOrdersByBranchId(branchId));
    }

    // DELETE /api/orders/{id} — ROLE_STORE_MANAGER, ROLE_STORE_ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long id
    ) throws OrderException {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}