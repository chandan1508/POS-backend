package com.chandan.pos.mapper;

import com.chandan.pos.modal.Branch;
import com.chandan.pos.modal.Store;
import com.chandan.pos.payload.dto.BranchDTO;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    /**
     * Maps a BranchRequestDTO to a Branch entity.
     * Note: store and manager must be resolved and set separately via their
     * repositories.
     */
    public static Branch toEntity(BranchDTO dto, Store store) {
        Branch branch = new Branch();
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setWorkingDays(dto.getWorkingDays());
        branch.setOpenTime(dto.getOpenTime());
        branch.setCloseTime(dto.getCloseTime());
        branch.setStore(store);
        branch.setCreatedAt(LocalDateTime.now());
        branch.setUpdatedAt(LocalDateTime.now());
        return branch;
    }

    /**
     * Maps a Branch entity to a BranchResponseDTO.
     */
    public static BranchDTO toDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setAddress(branch.getAddress());
        dto.setPhone(branch.getPhone());
        dto.setEmail(branch.getEmail());
        dto.setWorkingDays(branch.getWorkingDays());
        dto.setOpenTime(branch.getOpenTime());
        dto.setCloseTime(branch.getCloseTime());
        dto.setCreatedAt(branch.getCreatedAt());
        dto.setUpdatedAt(branch.getUpdatedAt());

        dto.setStoreId(branch.getStore() != null ? branch.getStore().getId() : null);

        return dto;
    }
}
