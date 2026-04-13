package com.chandan.pos.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chandan.pos.domain.OrderStatus;
import com.chandan.pos.domain.PaymentType;
import com.chandan.pos.exceptions.OrderException;
import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.mapper.OrderMapper;
import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Customer;
import com.chandan.pos.modal.Order;
import com.chandan.pos.modal.OrderItem;
import com.chandan.pos.modal.Product;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.OrderDto;
import com.chandan.pos.repository.BranchRepository;
import com.chandan.pos.repository.CustomerRepository;
import com.chandan.pos.repository.OrderRepository;
import com.chandan.pos.repository.ProductRepository;
import com.chandan.pos.service.OrderService;
import com.chandan.pos.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository    orderRepository;
    private final UserService        userService;
    private final BranchRepository   branchRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository  productRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) throws OrderException {

        // Resolve cashier from JWT session
        User cashier;
        try {
            cashier = userService.getCurrentUser();
        } catch (UserException e) {
            throw new OrderException("Could not resolve cashier: " + e.getMessage());
        }

        // Resolve branch from cashier's assigned branch
        Branch branch = branchRepository.findById(cashier.getBranch().getId())
                .orElseThrow(() -> new OrderException("Branch not found for cashier"));

        // Resolve customer (optional)
        Customer customer = null;
        if (orderDto.getCustomerId() != null) {
            customer = customerRepository.findById(orderDto.getCustomerId())
                    .orElseThrow(() -> new OrderException(
                            "Customer not found with id: " + orderDto.getCustomerId()));
        }

        // Build order first (needed to link OrderItems)
        Order order = new Order();
        order.setBranch(branch);
        order.setCashier(cashier);
        order.setCustomer(customer);
        order.setPaymentType(orderDto.getPaymentType());
        order.setOrderStatus(OrderStatus.PENDING);

        // Resolve and build each OrderItem with Product
        List<OrderItem> items = orderDto.getItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id: " + itemDto.getProductId()));

            OrderItem item = new OrderItem();
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getSellingPrice() * itemDto.getQuantity());
            item.setProduct(product);
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        // Calculate total from resolved item prices
        double total = items.stream().mapToDouble(OrderItem::getPrice).sum();

        order.setItems(items);
        order.setTotalAmount(total);

        return OrderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public OrderDto getOrderById(Long id) throws OrderException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("Order not found with id: " + id));
        return OrderMapper.toDTO(order);
    }

    @Override
    public List<OrderDto> getOrdersByBranch(
            Long branchId, Long customerId, Long cashierId,
            PaymentType paymentType, OrderStatus orderStatus
    ) {
        return orderRepository
                .findByBranchWithFilters(branchId, customerId, cashierId, paymentType, orderStatus)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCashier(Long cashierId) {
        return orderRepository.findByCashierId(cashierId)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getTodayOrdersByBranch(Long branchId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay   = startOfDay.plusDays(1);
        return orderRepository.findTodayOrdersByBranch(branchId, startOfDay, endOfDay)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getTop5RecentOrdersByBranchId(Long branchId) {
        return orderRepository.findTop5RecentByBranchId(branchId)
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) throws OrderException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("Order not found with id: " + id));
        orderRepository.delete(order);
    }
}