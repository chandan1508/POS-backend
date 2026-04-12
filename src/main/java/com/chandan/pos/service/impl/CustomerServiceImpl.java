package com.chandan.pos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chandan.pos.exceptions.CustomerException;
import com.chandan.pos.mapper.CustomerMapper;
import com.chandan.pos.modal.Customer;
import com.chandan.pos.payload.dto.CustomerDto;
import com.chandan.pos.repository.CustomerRepository;
import com.chandan.pos.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(CustomerDto customerDto) throws CustomerException {
        Customer customer = CustomerMapper.toEntity(customerDto);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, CustomerDto customerDto) throws CustomerException {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found with id: " + id));

        existing.setFullName(customerDto.getFullName());
        existing.setEmail(customerDto.getEmail());
        existing.setPhone(customerDto.getPhone());

        return customerRepository.save(existing);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerException {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found with id: " + id));
        customerRepository.delete(existing);
    }

    @Override
    public Customer getCustomerById(Long id) throws CustomerException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerException("Customer not found with id: " + id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

     @Override
    public List<Customer> searchCustomers(String query) {
        return customerRepository.searchCustomers(query);
    }
}