package com.chandan.pos.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chandan.pos.exceptions.RefundException;
import com.chandan.pos.payload.dto.RefundDto;
import com.chandan.pos.service.RefundService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    // POST /api/refunds
    @PostMapping
    public ResponseEntity<RefundDto> createRefund(
            @Valid @RequestBody RefundDto refundDto
    ) throws RefundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(refundService.createRefund(refundDto));
    }

    // GET /api/refunds
    @GetMapping
    public ResponseEntity<List<RefundDto>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    // GET /api/refunds/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RefundDto> getRefundById(
            @PathVariable Long id
    ) throws RefundException {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }

    // GET /api/refunds/cashier/{cashierId}
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<RefundDto>> getRefundsByCashier(
            @PathVariable Long cashierId
    ) {
        return ResponseEntity.ok(refundService.getRefundsByCashier(cashierId));
    }

    // GET /api/refunds/branch/{branchId}
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RefundDto>> getRefundsByBranch(
            @PathVariable Long branchId
    ) {
        return ResponseEntity.ok(refundService.getRefundsByBranch(branchId));
    }

    // GET /api/refunds/shift/{shiftReportId}
    @GetMapping("/shift/{shiftReportId}")
    public ResponseEntity<List<RefundDto>> getRefundsByShiftReport(
            @PathVariable Long shiftReportId
    ) {
        return ResponseEntity.ok(refundService.getRefundsByShiftReport(shiftReportId));
    }

    // GET /api/refunds/cashier/{cashierId}/range?from=...&to=...
    @GetMapping("/cashier/{cashierId}/range")
    public ResponseEntity<List<RefundDto>> getRefundsByCashierAndDateRange(
            @PathVariable Long cashierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(
                refundService.getRefundsByCashierAndDateRange(cashierId, from, to)
        );
    }

    // DELETE /api/refunds/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRefund(
            @PathVariable Long id
    ) throws RefundException {
        refundService.deleteRefund(id);
        return ResponseEntity.ok("Refund deleted successfully");
    }
}