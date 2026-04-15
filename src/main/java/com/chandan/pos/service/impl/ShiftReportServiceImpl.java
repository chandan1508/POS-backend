package com.chandan.pos.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chandan.pos.domain.PaymentType;
import com.chandan.pos.exceptions.ShiftReportException;
import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.mapper.ShiftReportMapper;
import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Order;
import com.chandan.pos.modal.PaymentSummary;
import com.chandan.pos.modal.Product;
import com.chandan.pos.modal.Refund;
import com.chandan.pos.modal.ShiftReport;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.ShiftReportDto;
import com.chandan.pos.repository.BranchRepository;
import com.chandan.pos.repository.OrderRepository;
import com.chandan.pos.repository.RefundRepository;
import com.chandan.pos.repository.ShiftReportRepository;
import com.chandan.pos.service.ShiftReportService;
import com.chandan.pos.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftReportServiceImpl implements ShiftReportService {

    private final ShiftReportRepository shiftReportRepository;
    private final BranchRepository      branchRepository;
    private final OrderRepository       orderRepository;
    private final RefundRepository      refundRepository;
    private final UserService           userService;

    // -------------------------------------------------------------------------
    // Start shift
    // -------------------------------------------------------------------------

    @Override
    public ShiftReportDto startShift(Long branchId) throws ShiftReportException {

        User cashier = resolveCashier();

        // Prevent opening a second concurrent shift
        shiftReportRepository.findByCashierIdAndShiftEndIsNull(cashier.getId())
                .ifPresent(s -> {
                    throw new IllegalStateException(
                            "Cashier already has an active shift (id: " + s.getId() + ")");
                });

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ShiftReportException(
                        "Branch not found with id: " + branchId));

        ShiftReport report = new ShiftReport();
        report.setCashier(cashier);
        report.setBranch(branch);
        report.setShiftStart(LocalDateTime.now());
        report.setTotalSales(0.0);
        report.setTotalRefunds(0.0);
        report.setNetSale(0.0);
        report.setTotalOrders(0.0);

        return ShiftReportMapper.toDTO(shiftReportRepository.save(report));
    }

    // -------------------------------------------------------------------------
    // End shift
    // -------------------------------------------------------------------------

    @Override
    public ShiftReportDto endShift() throws ShiftReportException {

        User cashier = resolveCashier();

        ShiftReport report = shiftReportRepository
                .findByCashierIdAndShiftEndIsNull(cashier.getId())
                .orElseThrow(() -> new ShiftReportException(
                        "No active shift found for cashier id: " + cashier.getId()));

        report.setShiftEnd(LocalDateTime.now());

        // Recalculate aggregates before closing
        populateAggregates(report);

        return ShiftReportMapper.toDTO(shiftReportRepository.save(report));
    }

    // -------------------------------------------------------------------------
    // Current shift (live)
    // -------------------------------------------------------------------------

    @Override
    public ShiftReportDto getCurrentShift() throws ShiftReportException {

        User cashier = resolveCashier();

        ShiftReport report = shiftReportRepository
                .findByCashierIdAndShiftEndIsNull(cashier.getId())
                .orElseThrow(() -> new ShiftReportException(
                        "No active shift found for cashier id: " + cashier.getId()));

        // Refresh live aggregates without saving
        populateAggregates(report);

        return ShiftReportMapper.toDTO(report);
    }

    // -------------------------------------------------------------------------
    // Read operations
    // -------------------------------------------------------------------------

    @Override
    public List<ShiftReportDto> getAllShiftReports() {
        return shiftReportRepository.findAll()
                .stream()
                .map(ShiftReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDto> getShiftReportsByCashier(Long cashierId) {
        return shiftReportRepository.findByCashierId(cashierId)
                .stream()
                .map(ShiftReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDto> getShiftReportsByBranch(Long branchId) {
        return shiftReportRepository.findByBranchId(branchId)
                .stream()
                .map(ShiftReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShiftReportDto getShiftReportByCashierAndDate(Long cashierId, LocalDate date)
            throws ShiftReportException {
        ShiftReport report = shiftReportRepository
                .findByCashierIdAndDate(cashierId, date)
                .orElseThrow(() -> new ShiftReportException(
                        "No shift report found for cashier " + cashierId + " on " + date));
        return ShiftReportMapper.toDTO(report);
    }

    @Override
    public ShiftReportDto getShiftReportById(Long id) throws ShiftReportException {
        ShiftReport report = shiftReportRepository.findById(id)
                .orElseThrow(() -> new ShiftReportException(
                        "Shift report not found with id: " + id));
        return ShiftReportMapper.toDTO(report);
    }

    // -------------------------------------------------------------------------
    // Delete
    // -------------------------------------------------------------------------

    @Override
    public void deleteShiftReport(Long id) throws ShiftReportException {
        ShiftReport report = shiftReportRepository.findById(id)
                .orElseThrow(() -> new ShiftReportException(
                        "Shift report not found with id: " + id));
        shiftReportRepository.delete(report);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private User resolveCashier() throws ShiftReportException {
        try {
            return userService.getCurrentUser();
        } catch (UserException e) {
            throw new ShiftReportException("Could not resolve cashier: " + e.getMessage());
        }
    }

    /**
     * Recalculates all aggregate fields on the given report from the DB.
     * Orders and refunds are scoped to the shift's time window.
     */
    private void populateAggregates(ShiftReport report) {

        LocalDateTime from = report.getShiftStart();
        LocalDateTime to   = report.getShiftEnd() != null
                             ? report.getShiftEnd()
                             : LocalDateTime.now();

        Long cashierId = report.getCashier().getId();
        Long branchId  = report.getBranch().getId();

        // Orders within the shift window
        List<Order> orders = orderRepository
                .findByCashierIdAndBranchIdAndCreatedAtBetween(cashierId, branchId, from, to);

        // Refunds within the shift window
        List<Refund> refunds = refundRepository
                .findByCashierIdAndCreatedAtBetween(cashierId, from, to);

        double totalSales   = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        double totalRefunds = refunds.stream()
                .mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0.0)
                .sum();

        report.setTotalSales(totalSales);
        report.setTotalRefunds(totalRefunds);
        report.setNetSale(totalSales - totalRefunds);
        report.setTotalOrders((double) orders.size());
        report.setRefunds(refunds);

        // Payment summaries grouped by payment type
        Map<PaymentType, List<Order>> byPaymentType = orders.stream()
                .filter(o -> o.getPaymentType() != null)
                .collect(Collectors.groupingBy(Order::getPaymentType));

        List<PaymentSummary> summaries = byPaymentType.entrySet().stream()
                .map(entry -> {
                    PaymentSummary ps = new PaymentSummary();
                    ps.setType(entry.getKey());
                    double typeTotal = entry.getValue().stream()
                            .mapToDouble(Order::getTotalAmount).sum();
                    ps.setTotalAmount(typeTotal);
                    ps.setTransactionCount(entry.getValue().size());
                    ps.setPercentage(totalSales > 0 ? (typeTotal / totalSales) * 100 : 0);
                    return ps;
                })
                .collect(Collectors.toList());

        report.setPaymentSummaries(summaries);

        // Top-selling products (top 5 by quantity sold)
        List<Product> topProducts = orders.stream()
                .flatMap(o -> o.getItems() != null ? o.getItems().stream() : java.util.stream.Stream.empty())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct(),
                        Collectors.summingInt(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        report.setTopSellingProducts(topProducts);

        // Recent orders (last 10)
        List<Order> recentOrders = orders.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(10)
                .collect(Collectors.toList());

        report.setRecentOrders(recentOrders);
    }
}