package com.chandan.pos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chandan.pos.modal.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByFullNameContainingIgnoreCase(String fullName);

    @Query("""
            SELECT c FROM Customer c
            WHERE (:query IS NULL OR :query = '' OR
                   LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                   LOWER(c.email)    LIKE LOWER(CONCAT('%', :query, '%')) OR
                   c.phone           LIKE CONCAT('%', :query, '%'))
            ORDER BY c.fullName ASC
            """)
    List<Customer> searchCustomers(@Param("query") String query);
}