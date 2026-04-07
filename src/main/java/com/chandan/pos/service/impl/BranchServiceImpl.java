package com.chandan.pos.service.impl;

import com.chandan.pos.exceptions.ResourceNotFoundException;
import com.chandan.pos.mapper.BranchMapper;
import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.BranchDTO;
import com.chandan.pos.repository.BranchRepository;
import com.chandan.pos.repository.StoreRepository;
import com.chandan.pos.service.BranchService;
import com.chandan.pos.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    

    @Override
    public BranchDTO createBranch(BranchDTO branchDTO) {
        User currentUser = userService.getCurrentUser();

        System.out.println("currentUser :"+ currentUser.getId());

        Store store = storeRepository.findByStoreAdminId(currentUser.getId());

        Branch branch = BranchMapper.toEntity(branchDTO, store);
        Branch saved = branchRepository.save(branch);
        return BranchMapper.toDTO(saved);
    }

    @Override
    public BranchDTO getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        return BranchMapper.toDTO(branch);
    }

    @Override
    public List<BranchDTO> getAllBranchesByStoreId(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new ResourceNotFoundException("Store not found with id: " + storeId);
        }
        return branchRepository.findByStoreId(storeId)
                .stream()
                .map(BranchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BranchDTO updateBranch(Long id, BranchDTO branchDTO) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

        branch.setName(branchDTO.getName());
        branch.setAddress(branchDTO.getAddress());
        branch.setPhone(branchDTO.getPhone());
        branch.setEmail(branchDTO.getEmail());
        branch.setWorkingDays(branchDTO.getWorkingDays());
        branch.setOpenTime(branchDTO.getOpenTime());
        branch.setCloseTime(branchDTO.getCloseTime());
        branch.setUpdatedAt(LocalDateTime.now());
        
        Branch updatedBranch = branchRepository.save(branch);
        return BranchMapper.toDTO(updatedBranch);
       
    }

    @Override
    public void deleteBranch(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found with id: " + id);
        }
        branchRepository.deleteById(id);
    }
}
