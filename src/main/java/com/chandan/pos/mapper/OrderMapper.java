package com.chandan.pos.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.chandan.pos.modal.Order;
import com.chandan.pos.modal.OrderItem;
import com.chandan.pos.payload.dto.OrderDto;
import com.chandan.pos.payload.dto.OrderItemDto;
import com.chandan.pos.payload.dto.ProductDTO;

public class OrderMapper {

    public static OrderDto toDTO(Order order) {
        OrderDto dto = new OrderDto();

        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setPaymentType(order.getPaymentType());

        if (order.getBranch() != null) {
            dto.setBranchId(order.getBranch().getId());
            dto.setBranch(BranchMapper.toDTO(order.getBranch()));
        }

        if (order.getCashier() != null) {
            dto.setCashier(UserMapper.toDTO(order.getCashier()));
        }

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomer(order.getCustomer());
        }

        if (order.getItems() != null) {
            dto.setItems(order.getItems()
                    .stream()
                    .map(OrderMapper::toItemDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static OrderItemDto toItemDTO(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();

        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setOrderId(item.getOrder() != null ? item.getOrder().getId() : null);

        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(item.getProduct().getId());
            productDTO.setName(item.getProduct().getName());
            productDTO.setSellingPrice(item.getProduct().getSellingPrice());
            dto.setProduct(productDTO);
        }

        return dto;
    }

    public static OrderItem toItemEntity(OrderItemDto dto) {
        OrderItem item = new OrderItem();
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        // product and order are resolved in service layer
        return item;
    }

    public static List<OrderItem> toItemEntities(List<OrderItemDto> dtos) {
        return dtos.stream()
                .map(OrderMapper::toItemEntity)
                .collect(Collectors.toList());
    }
}