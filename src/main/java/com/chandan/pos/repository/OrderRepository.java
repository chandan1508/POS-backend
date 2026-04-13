package com.chandan.pos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chandan.pos.domain.OrderStatus;
import com.chandan.pos.domain.PaymentType;
import com.chandan.pos.modal.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCashierId(Long cashierId);

    List<Order> findByCustomerId(Long customerId);

    // Branch orders with optional filters: customerId, cashierId, paymentType, orderStatus
    @Query("""
            SELECT o FROM Order o
            WHERE o.branch.id = :branchId
              AND (:customerId   IS NULL OR o.customer.id   = :customerId)
              AND (:cashierId    IS NULL OR o.cashier.id    = :cashierId)
              AND (:paymentType  IS NULL OR o.paymentType   = :paymentType)
              AND (:orderStatus  IS NULL OR o.orderStatus   = :orderStatus)
            ORDER BY o.createdAt DESC
            """)
    List<Order> findByBranchWithFilters(
            @Param("branchId")    Long branchId,
            @Param("customerId")  Long customerId,
            @Param("cashierId")   Long cashierId,
            @Param("paymentType") PaymentType paymentType,
            @Param("orderStatus") OrderStatus orderStatus
    );

    // Today's orders for a branch
    @Query("""
            SELECT o FROM Order o
            WHERE o.branch.id = :branchId
              AND o.createdAt >= :startOfDay
              AND o.createdAt <  :endOfDay
            ORDER BY o.createdAt DESC
            """)
    List<Order> findTodayOrdersByBranch(
            @Param("branchId")   Long branchId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay")   LocalDateTime endOfDay
    );

    // Top 5 most recent orders for a branch
    @Query("""
            SELECT o FROM Order o
            WHERE o.branch.id = :branchId
            ORDER BY o.createdAt DESC
            LIMIT 5
            """)
    List<Order> findTop5RecentByBranchId(@Param("branchId") Long branchId);
}