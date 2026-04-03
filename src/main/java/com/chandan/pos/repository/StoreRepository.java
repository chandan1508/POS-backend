package com.chandan.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chandan.pos.modal.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
     Store findByStoreAdminId(Long adminId);
}
