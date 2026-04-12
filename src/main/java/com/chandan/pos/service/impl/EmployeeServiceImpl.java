package com.chandan.pos.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chandan.pos.domain.UserRole;
import com.chandan.pos.mapper.UserMapper;
import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.UserDto;
import com.chandan.pos.repository.BranchRepository;
import com.chandan.pos.repository.StoreRepository;
import com.chandan.pos.repository.UserRepository;
import com.chandan.pos.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createStoreEmployee(UserDto employeeDto, Long storeId) throws Exception {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new Exception("Store not found with id: " + storeId));

        Branch branch=null;

        if(employeeDto.getRole()==UserRole.ROLE_BRANCH_MANAGER){
            if(employeeDto.getBranchId()==null){
                throw new Exception("branch it is required to create branch manager");
            }

            branch = branchRepository.findById(employeeDto.getBranchId())
                .orElseThrow(() -> new Exception("Branch not found with id: " + employeeDto.getBranchId()));
        }

        User employee = UserMapper.toEntity(employeeDto);
        employee.setStore(store);
        employee.setBranch(branch);
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
      

        User saved = userRepository.save(employee);
        if(employeeDto.getRole()==UserRole.ROLE_BRANCH_MANAGER && branch!=null){
            branch.setManager(saved);
            branchRepository.save(branch);
        }
        return UserMapper.toDTO(saved);
    }

    @Override
    public UserDto createBranchEmployee(UserDto employeeDto, Long branchId) throws Exception {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch not found with id: " + branchId));

        if(employeeDto.getRole()==UserRole.ROLE_BRANCH_CASHIER || 
          employeeDto.getRole()==UserRole.ROLE_BRANCH_MANAGER ){
             User employee = UserMapper.toEntity(employeeDto);
        employee.setBranch(branch);
      
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
       

        User saved = userRepository.save(employee);
        
        return UserMapper.toDTO(saved);
        }

       throw new Exception("branch role not supported");
    }

    @Override
    public User updateEmployee(Long employeeId, UserDto employeeDetails) throws Exception {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        Branch branch = branchRepository.findById(employeeDetails.getBranchId())
                .orElseThrow(() -> new Exception("Branch not found with id: " + employeeDetails.getBranchId()));

        if (employeeDetails.getFullName() != null) {
            employee.setFullName(employeeDetails.getFullName());
        }
        if (employeeDetails.getPhone() != null) {
            employee.setPhone(employeeDetails.getPhone());
        }
        if (employeeDetails.getRole() != null) {
            employee.setRole(employeeDetails.getRole());
        }
        if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isBlank()) {
            employee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
        }
        
        if(branch!=null){
            employee.setBranch(branch);
        }
        employee.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        userRepository.delete(employee);
    }

    @Override
    public List<UserDto> findStoreEmployees(Long storeId, UserRole role) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        if (role != null) {
            return userRepository.findByStoreAndRole(store, role).stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
        }
        return userRepository.findByStore(store).stream().map(UserMapper::toDTO)
        .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findBranchEmployees(Long branchId, UserRole role) {
        if (!branchRepository.existsById(branchId)) {
            throw new RuntimeException("Branch not found with id: " + branchId);
        }

        if (role != null) {
            return userRepository.findByBranchIdAndRole(branchId, role).stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
        }
        return userRepository.findByBranchId(branchId).stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }
}