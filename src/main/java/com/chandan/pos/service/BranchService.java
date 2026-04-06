package com.chandan.pos.service;

import java.util.List;

import com.chandan.pos.payload.dto.BranchDTO;

public interface BranchService {

    BranchDTO createBranch(BranchDTO requestDTO);

    BranchDTO updateBranch(Long id, BranchDTO requestDTO);

    void deleteBranch(Long id);

    List<BranchDTO> getAllBranchesByStoreId(Long storeId);

    BranchDTO getBranchById(Long id);

}