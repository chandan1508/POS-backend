package com.chandan.pos.service.impl;

import com.chandan.pos.exceptions.ResourceNotFoundException;
import com.chandan.pos.mapper.InventoryMapper;
import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Inventory;
import com.chandan.pos.modal.Product;
import com.chandan.pos.payload.dto.InventoryDTO;
import com.chandan.pos.repository.BranchRepository;
import com.chandan.pos.repository.InventoryRepository;
import com.chandan.pos.repository.ProductRepository;
import com.chandan.pos.service.InventoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        Branch branch = branchRepository.findById(inventoryDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch not found with id: " + inventoryDTO.getBranchId()));

        Product product = productRepository.findById(inventoryDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + inventoryDTO.getProductId()));

        Inventory inventory = InventoryMapper.toEntity(inventoryDTO, branch, product);
        Inventory saved = inventoryRepository.save(inventory);
        return InventoryMapper.toDTO(saved);
    }

    @Override
    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found with id: " + id));

        inventory.setQuantity(inventoryDTO.getQuantity());

        Inventory updated = inventoryRepository.save(inventory);
        return InventoryMapper.toDTO(updated);
    }

    @Override
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    @Override
    public InventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found with id: " + id));
        return InventoryMapper.toDTO(inventory);
    }

    @Override
    public InventoryDTO getInventoryByProductIdAndBranchId(Long productId, Long branchId) {
        Inventory inventory= inventoryRepository.findByProductIdAndBranchId(productId, branchId);
                return InventoryMapper.toDTO(inventory);
    }

    @Override
    public List<InventoryDTO> getInventoryByBranchId(Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new ResourceNotFoundException("Branch not found with id: " + branchId);
        }
        return inventoryRepository.findByBranchId(branchId)
                .stream()
                .map(InventoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}