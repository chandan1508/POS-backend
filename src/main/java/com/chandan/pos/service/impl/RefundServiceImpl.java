package com.chandan.pos.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chandan.pos.exceptions.RefundException;
import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.mapper.RefundMapper;
import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Order;
import com.chandan.pos.modal.Refund;
import com.chandan.pos.modal.ShiftReport;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.RefundDto;
import com.chandan.pos.repository.BranchRepository;
import com.chandan.pos.repository.OrderRepository;
import com.chandan.pos.repository.RefundRepository;
import com.chandan.pos.repository.ShiftReportRepository;
import com.chandan.pos.service.RefundService;
import com.chandan.pos.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository     refundRepository;
    private final OrderRepository      orderRepository;
    private final BranchRepository     branchRepository;
    private final ShiftReportRepository shiftReportRepository;
    private final UserService          userService;

    @Override
    public RefundDto createRefund(RefundDto refundDto) throws RefundException {

        // Resolve cashier from JWT session
        User cashier;
        try {
            cashier = userService.getCurrentUser();
        } catch (UserException e) {
            throw new RefundException("Could not resolve cashier: " + e.getMessage());
        }

        // Resolve order
        Order order = orderRepository.findById(refundDto.getOrderId())
                .orElseThrow(() -> new RefundException(
                        "Order not found with id: " + refundDto.getOrderId()));

        // Resolve branch from cashier's assigned branch
        Branch branch = branchRepository.findById(cashier.getBranch().getId())
                .orElseThrow(() -> new RefundException("Branch not found for cashier"));

        // Resolve shift report (optional)
        ShiftReport shiftReport = null;
        if (refundDto.getShiftReportId() != null) {
            shiftReport = shiftReportRepository.findById(refundDto.getShiftReportId())
                    .orElseThrow(() -> new RefundException(
                            "Shift report not found with id: " + refundDto.getShiftReportId()));
        }

        // Validate refund amount does not exceed order total
        if (refundDto.getAmount() > order.getTotalAmount()) {
            throw new RefundException(
                    "Refund amount cannot exceed order total of: " + order.getTotalAmount());
        }

        Refund refund = new Refund();
        refund.setOrder(order);
        refund.setReason(refundDto.getReason());
        refund.setAmount(refundDto.getAmount());
        refund.setCashier(cashier);
        refund.setBranch(branch);
        refund.setShiftReport(shiftReport);
        refund.setPaymentType(refundDto.getPaymentType());

        return RefundMapper.toDTO(refundRepository.save(refund));
    }

    @Override
    public List<RefundDto> getAllRefunds() {
        return refundRepository.findAll()
                .stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByCashier(Long cashierId) {
        return refundRepository.findByCashierId(cashierId)
                .stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByBranch(Long branchId) {
        return refundRepository.findByBranchId(branchId)
                .stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByShiftReport(Long shiftReportId) {
        return refundRepository.findByShiftReportId(shiftReportId)
                .stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByCashierAndDateRange(
            Long cashierId, LocalDateTime from, LocalDateTime to
    ) {
        return refundRepository.findByCashierIdAndCreatedAtBetween(cashierId, from, to)
                .stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RefundDto getRefundById(Long id) throws RefundException {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RefundException("Refund not found with id: " + id));
        return RefundMapper.toDTO(refund);
    }

    @Override
    public void deleteRefund(Long id) throws RefundException {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RefundException("Refund not found with id: " + id));
        refundRepository.delete(refund);
    }
}