package com.chandan.pos.controller;

import com.chandan.pos.payload.dto.BranchDTO;
import com.chandan.pos.service.BranchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    /**
     * Create Branch
     */
    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(
            @RequestBody BranchDTO requestDTO
    ) {
        BranchDTO response = branchService.createBranch(requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Get Branch By ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id) {
        BranchDTO response = branchService.getBranchById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get All Branches By Store ID
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<BranchDTO>> getAllBranchesByStoreId(
            @PathVariable Long storeId
    ) {
        List<BranchDTO> response = branchService.getAllBranchesByStoreId(storeId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update Branch
     */
    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> updateBranch(
            @PathVariable Long id,
            @Valid @RequestBody BranchDTO requestDTO
    ) {
        BranchDTO response = branchService.updateBranch(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete Branch
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}