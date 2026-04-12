package com.chandan.pos.service;

import java.util.List;

import com.chandan.pos.domain.UserRole;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.UserDto;

public interface EmployeeService {
    UserDto createStoreEmployee(UserDto employee, Long storeId) throws Exception;
    UserDto createBranchEmployee(UserDto employee, Long branchId) throws Exception;
    User updateEmployee(Long employeeId, UserDto employeeDetails) throws Exception;
    void deleteEmployee(Long employeeId);
    List<UserDto> findStoreEmployees(Long storeId, UserRole role);
    List<UserDto> findBranchEmployees(Long branchId, UserRole role);
}
