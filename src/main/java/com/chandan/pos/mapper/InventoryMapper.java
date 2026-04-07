package com.chandan.pos.mapper;

import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Inventory;
import com.chandan.pos.modal.Product;
import com.chandan.pos.payload.dto.InventoryDTO;

import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public static Inventory toEntity(InventoryDTO dto, Branch branch, Product product) {
        Inventory inventory = new Inventory();
        inventory.setBranch(branch);
        inventory.setProduct(product);
        inventory.setQuantity(dto.getQuantity());
        return inventory;
    }

    public static InventoryDTO toDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setBranchId(inventory.getBranch() != null ? inventory.getBranch().getId() : null);
        dto.setProductId(inventory.getProduct() != null ? inventory.getProduct().getId() : null);
        dto.setProduct(ProductMapper.toDTO(inventory.getProduct()));
        dto.setQuantity(inventory.getQuantity());
        dto.setLastUpdated(inventory.getLastUpdated());
        return dto;
    }
}