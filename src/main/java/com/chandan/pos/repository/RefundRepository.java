package com.chandan.pos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chandan.pos.modal.Refund;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    List<Refund> findByCashierId(Long cashierId);

    List<Refund> findByBranchId(Long branchId);

    List<Refund> findByShiftReportId(Long shiftReportId);

    // Refunds by cashier within a date/time range
    @Query("""
            SELECT r FROM Refund r
            WHERE r.cashier.id = :cashierId
              AND r.createdAt >= :from
              AND r.createdAt <= :to
            ORDER BY r.createdAt DESC
            """)
    List<Refund> findByCashierIdAndCreatedAtBetween(
            @Param("cashierId") Long cashierId,
            @Param("from")      LocalDateTime from,
            @Param("to")        LocalDateTime to
    );
}