package com.chandan.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chandan.pos.domain.UserRole;
import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findByStore(Store store);

    List<User> findByBranchId(Long branchId);

    List<User> findByStoreAndRole(Store store, UserRole role);

    List<User> findByBranchIdAndRole(Long branchId, UserRole role);

}
