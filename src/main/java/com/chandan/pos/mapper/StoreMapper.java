package com.chandan.pos.mapper;

import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.StoreDTO;

public class StoreMapper {

    public static StoreDTO toDTO(Store store) {
        if (store == null)
            return null;

        StoreDTO dto = new StoreDTO();

        dto.setId(store.getId());
        dto.setBrand(store.getBrand());
        dto.setDescription(store.getDescription());
        dto.setStoreType(store.getStoreType());
        dto.setStatus(store.getStatus());
        dto.setCreatedAt(store.getCreatedAt());
        dto.setUpdatedAt(store.getUpdatedAt());

        dto.setStoreAdmin(UserMapper.toDTO(store.getStoreAdmin()));

        dto.setContact(store.getContact()); // direct mapping if same object

        return dto;
    }

    public static Store toEntity(StoreDTO dto, User storeAdmin) {
        if (dto == null)
            return null;

        Store store = new Store();

        store.setId(dto.getId());
        store.setBrand(dto.getBrand());
        store.setDescription(dto.getDescription());
        store.setStoreType(dto.getStoreType());
        store.setStatus(dto.getStatus());
        store.setCreatedAt(dto.getCreatedAt());
        store.setUpdatedAt(dto.getUpdatedAt());

        store.setStoreAdmin(storeAdmin);

        store.setContact(dto.getContact());

        return store;
    }
}