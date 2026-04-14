package com.chandan.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chandan.pos.modal.ShiftReport;

public interface ShiftReportRepository extends JpaRepository<ShiftReport, Long> {

    
}
