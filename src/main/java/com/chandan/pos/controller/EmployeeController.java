package com.chandan.pos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.chandan.pos.domain.UserRole;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.UserDto;
import com.chandan.pos.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/store/{storeId}")
    @PreAuthorize("hasAnyRole('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<UserDto> createStoreEmployee(
            @RequestBody UserDto employeeDto,
            @PathVariable Long storeId) throws Exception {
        UserDto created = employeeService.createStoreEmployee(employeeDto, storeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/branch/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_BRANCH_MANAGER', 'ROLE_STORE_ADMIN')")
    public ResponseEntity<UserDto> createBranchEmployee(
            @RequestBody UserDto employeeDto,
            @PathVariable Long branchId) throws Exception {
        UserDto created = employeeService.createBranchEmployee(employeeDto, branchId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ✅ Changed: @RequestBody UserDto (was User), added throws Exception
    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<User> updateEmployee(
            @PathVariable Long employeeId,
            @RequestBody UserDto employeeDetails) throws Exception {
        User updated = employeeService.updateEmployee(employeeId, employeeDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_STORE_ADMIN', 'ROLE_BRANCH_MANAGER')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasAnyRole('ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<List<UserDto>> getStoreEmployees(
            @PathVariable Long storeId,
            @RequestParam(required = false) UserRole role) {
        return ResponseEntity.ok(employeeService.findStoreEmployees(storeId, role));
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasAnyRole('ROLE_BRANCH_MANAGER', 'ROLE_STORE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<List<UserDto>> getBranchEmployees(
            @PathVariable Long branchId,
            @RequestParam(required = false) UserRole role) {
        return ResponseEntity.ok(employeeService.findBranchEmployees(branchId, role));
    }
}