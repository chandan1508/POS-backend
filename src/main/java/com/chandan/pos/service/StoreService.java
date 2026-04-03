package com.chandan.pos.service;

import java.util.List;

import com.chandan.pos.domain.StoreStatus;
import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.StoreDTO;

public interface StoreService  {
    StoreDTO createStore(StoreDTO storeDTO, User user);
    StoreDTO getStoreById(Long id) throws Exception;
    List<StoreDTO> getAllStores();
    Store getStoreByAdmin();
    StoreDTO updateStore(Long id, StoreDTO storeDTO) throws Exception;
    void deleteStore(Long id);
    StoreDTO getStoreByEmployee();
    // StoreDTO updateStore(Long id, StoreDTO storeDTO) throws Exception;
    StoreDTO moderateStore(Long id, StoreStatus status) throws Exception;
}
