package com.chandan.pos.service;

import java.util.List;

import com.chandan.pos.exceptions.CustomerException;
import com.chandan.pos.modal.Customer;
import com.chandan.pos.payload.dto.CustomerDto;

public interface CustomerService {

    Customer createCustomer(CustomerDto customerDto) throws CustomerException;

    Customer updateCustomer(Long id, CustomerDto customerDto) throws CustomerException;

    void deleteCustomer(Long id) throws CustomerException;

    Customer getCustomerById(Long id) throws CustomerException;

    List<Customer> getAllCustomers();

    List<Customer> searchCustomers(String query);
}