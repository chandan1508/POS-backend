package com.chandan.pos.mapper;

import com.chandan.pos.modal.Customer;
import com.chandan.pos.payload.dto.CustomerDto;

public class CustomerMapper {

    public static CustomerDto toDTO(Customer customer) {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setId(customer.getId());
        customerDto.setFullName(customer.getFullName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhone(customer.getPhone());
        customerDto.setCreatedAt(customer.getCreatedAt());
        customerDto.setUpdatedAt(customer.getUpdatedAt());

        return customerDto;
    }

    public static Customer toEntity(CustomerDto customerDto) {
        Customer customer = new Customer();

        customer.setFullName(customerDto.getFullName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());

        return customer;
    }
}