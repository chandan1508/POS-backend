package com.chandan.pos.service;

import java.time.LocalDateTime;
import java.util.List;

import com.chandan.pos.exceptions.RefundException;
import com.chandan.pos.payload.dto.RefundDto;

public interface RefundService {

    RefundDto createRefund(RefundDto refundDto) throws RefundException;

    List<RefundDto> getAllRefunds();

    List<RefundDto> getRefundsByCashier(Long cashierId);

    List<RefundDto> getRefundsByBranch(Long branchId);

    List<RefundDto> getRefundsByShiftReport(Long shiftReportId);

    List<RefundDto> getRefundsByCashierAndDateRange(Long cashierId, LocalDateTime from, LocalDateTime to);

    RefundDto getRefundById(Long id) throws RefundException;

    void deleteRefund(Long id) throws RefundException;
}