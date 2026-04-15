package com.chandan.pos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chandan.pos.exceptions.ShiftReportException;
import com.chandan.pos.payload.dto.ShiftReportDto;
import com.chandan.pos.service.ShiftReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shift-reports")
@RequiredArgsConstructor
public class ShiftReportController {

    private final ShiftReportService shiftReportService;

    /**
     * POST /api/shift-reports/start?branchId=1
     * Start a new shift for the currently authenticated cashier.
     */
    @PostMapping("/start")
    public ResponseEntity<ShiftReportDto> startShift(
            @RequestParam Long branchId) throws ShiftReportException {
        ShiftReportDto dto = shiftReportService.startShift(branchId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * PATCH /api/shift-reports/end
     * End the active shift for the currently authenticated cashier.
     */
    @PatchMapping("/end")
    public ResponseEntity<ShiftReportDto> endShift() throws ShiftReportException {
        return ResponseEntity.ok(shiftReportService.endShift());
    }

    /**
     * GET /api/shift-reports/current
     * Live progress details of the currently running shift.
     */
    @GetMapping("/current")
    public ResponseEntity<ShiftReportDto> getCurrentShift() throws ShiftReportException {
        return ResponseEntity.ok(shiftReportService.getCurrentShift());
    }

    /**
     * GET /api/shift-reports/cashier/{cashierId}/by-date?date=2024-06-01
     * Get a specific shift report for a cashier on a given date.
     */
    @GetMapping("/cashier/{cashierId}/by-date")
    public ResponseEntity<ShiftReportDto> getByDate(
            @PathVariable Long cashierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws ShiftReportException {
        return ResponseEntity.ok(
                shiftReportService.getShiftReportByCashierAndDate(cashierId, date));
    }

    /**
     * GET /api/shift-reports/cashier/{cashierId}
     * Fetch all shift reports belonging to a specific cashier.
     */
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<ShiftReportDto>> getByCashier(
            @PathVariable Long cashierId) {
        return ResponseEntity.ok(shiftReportService.getShiftReportsByCashier(cashierId));
    }

    /**
     * GET /api/shift-reports/branch/{branchId}
     * Get all shift reports for a particular branch.
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<ShiftReportDto>> getByBranch(
            @PathVariable Long branchId) {
        return ResponseEntity.ok(shiftReportService.getShiftReportsByBranch(branchId));
    }

    /**
     * GET /api/shift-reports
     * Retrieve all shift reports (admin).
     */
    @GetMapping
    public ResponseEntity<List<ShiftReportDto>> getAllShiftReports() {
        return ResponseEntity.ok(shiftReportService.getAllShiftReports());
    }

    /**
     * GET /api/shift-reports/{id}
     * Get a specific shift report by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShiftReportDto> getById(
            @PathVariable Long id) throws ShiftReportException {
        return ResponseEntity.ok(shiftReportService.getShiftReportById(id));
    }

    /**
     * DELETE /api/shift-reports/{id}
     * Delete a shift report by ID (admin).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShiftReport(
            @PathVariable Long id) throws ShiftReportException {
        shiftReportService.deleteShiftReport(id);
        return ResponseEntity.noContent().build();
    }
}