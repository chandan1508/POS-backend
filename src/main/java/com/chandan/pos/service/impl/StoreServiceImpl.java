package com.chandan.pos.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chandan.pos.domain.StoreStatus;
import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.mapper.StoreMapper;
import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.StoreContact;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.StoreDTO;
import com.chandan.pos.repository.StoreRepository;
import com.chandan.pos.service.StoreService;
import com.chandan.pos.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;

    @Override
    public StoreDTO createStore(StoreDTO storeDTO, User user) {
        Store store = StoreMapper.toEntity(storeDTO, user);

        return StoreMapper.toDTO(storeRepository.save(store));
    }

    @Override
    public StoreDTO getStoreById(Long id) throws Exception {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new Exception("store not found..."));

        return StoreMapper.toDTO(store);
    }

    @Override
    public List<StoreDTO> getAllStores() {
        List<Store> dtos = storeRepository.findAll();
        return dtos.stream().map(StoreMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Store getStoreByAdmin() {
        User admin = userService.getCurrentUser();
        return storeRepository.findByStoreAdminId(admin.getId());
    }

    @Override
    public StoreDTO updateStore(Long id, StoreDTO storeDTO) throws Exception {
        User currentUser=userService.getCurrentUser();

        Store existing = storeRepository.findByStoreAdminId(currentUser.getId());

        if(existing==null){
            throw new Exception("store not found");
        }

        existing.setBrand(storeDTO.getBrand());
        existing.setDescription(storeDTO.getDescription());

        if(storeDTO.getStoreType()!=null){
            existing.setStoreType(storeDTO.getStoreType());
        }

        if(storeDTO.getContact()!=null){
            StoreContact contact=StoreContact.builder()
                  .address(storeDTO.getContact().getAddress())
                  .phone(storeDTO.getContact().getPhone())
                  .email(storeDTO.getContact().getEmail())
                  .build();
            existing.setContact(contact);
        }

        Store updateStore=storeRepository.save(existing);
        return StoreMapper.toDTO(updateStore);
    }

    @Override
    public void deleteStore(Long id) {
        Store store = getStoreByAdmin();
        storeRepository.delete(store);
    }

    @Override
    public StoreDTO getStoreByEmployee() {
        User currentUser=userService.getCurrentUser();

        if(currentUser==null){
            throw new UserException("you don't have permission to access this store");
        }

        return StoreMapper.toDTO(currentUser.getStore());
    }

    @Override
    public StoreDTO moderateStore(Long id, StoreStatus status) throws Exception {
        Store store = storeRepository.findById(id).orElseThrow(
            ()-> new Exception("store not found...")
        );
        store.setStatus(status);
        Store updatedStore = storeRepository.save(store);
        return StoreMapper.toDTO(updatedStore);
    }

}
