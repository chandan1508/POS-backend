package com.chandan.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chandan.pos.modal.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
       
} 
