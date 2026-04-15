package com.chandan.pos.service;

import java.time.LocalDate;
import java.util.List;

import com.chandan.pos.exceptions.ShiftReportException;
import com.chandan.pos.payload.dto.ShiftReportDto;

public interface ShiftReportService {

    /** Start a new shift for the currently authenticated cashier. */
    ShiftReportDto startShift(Long branchId) throws ShiftReportException;

    /** End the currently active shift for the authenticated cashier. */
    ShiftReportDto endShift() throws ShiftReportException;

    /** Live progress details of the currently running shift. */
    ShiftReportDto getCurrentShift() throws ShiftReportException;

    /** All shift reports (admin view). */
    List<ShiftReportDto> getAllShiftReports();

    /** All shift reports for a specific cashier. */
    List<ShiftReportDto> getShiftReportsByCashier(Long cashierId);

    /** All shift reports for a specific branch. */
    List<ShiftReportDto> getShiftReportsByBranch(Long branchId);

    /** Shift report for a cashier on a given date. */
    ShiftReportDto getShiftReportByCashierAndDate(Long cashierId, LocalDate date)
            throws ShiftReportException;

    /** A single shift report by ID. */
    ShiftReportDto getShiftReportById(Long id) throws ShiftReportException;

    /** Delete a shift report by ID (admin). */
    void deleteShiftReport(Long id) throws ShiftReportException;
}