package com.chandan.pos.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chandan.pos.modal.ShiftReport;

public interface ShiftReportRepository extends JpaRepository<ShiftReport, Long> {

    List<ShiftReport> findByCashierId(Long cashierId);

    List<ShiftReport> findByBranchId(Long branchId);

    /**
     * Finds an active (open) shift for the given cashier — i.e. shiftEnd is null.
     */
    Optional<ShiftReport> findByCashierIdAndShiftEndIsNull(Long cashierId);

    /**
     * Finds a shift report for a cashier on a specific date.
     */
    @Query("""
            SELECT s FROM ShiftReport s
            WHERE s.cashier.id = :cashierId
              AND CAST(s.shiftStart AS date) = :date
            """)
    Optional<ShiftReport> findByCashierIdAndDate(
            @Param("cashierId") Long cashierId,
            @Param("date") LocalDate date);
}