package com.chandan.pos.controller;

import com.chandan.pos.payload.dto.InventoryDTO;
import com.chandan.pos.service.InventoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Create Inventory — Store Manager only
     */
    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(
            @RequestBody InventoryDTO inventoryDTO
    ) {
        InventoryDTO response = inventoryService.createInventory(inventoryDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Update Inventory quantity — Store Manager only
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(
            @PathVariable Long id,
            @RequestBody InventoryDTO inventoryDTO
    ) {
        InventoryDTO response = inventoryService.updateInventory(id, inventoryDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete Inventory — Store Manager only
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get Inventory by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id) {
        InventoryDTO response = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get Inventory by Product ID (across all branches)
     */
    @GetMapping("/branch/{branchId}/product/{productId}")
    public ResponseEntity<InventoryDTO> getInventoryByProductIdAndBranchId(
            @PathVariable Long productId,
            @PathVariable Long branchId
    ) {
        InventoryDTO response = inventoryService.getInventoryByProductIdAndBranchId(productId, branchId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all Inventory records for a specific Branch
     */
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByBranchId(
            @PathVariable Long branchId
    ) {
        List<InventoryDTO> response = inventoryService.getInventoryByBranchId(branchId);
        return ResponseEntity.ok(response);
    }
}