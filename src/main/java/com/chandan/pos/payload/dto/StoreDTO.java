package com.chandan.pos.payload.dto;

import java.time.LocalDateTime;

import com.chandan.pos.domain.StoreStatus;
import com.chandan.pos.modal.StoreContact;

import lombok.Data;

@Data
public class StoreDTO {
    
    private Long id;

    
    private String brand;

    
    private UserDto storeAdmin;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String description;
    
    private String storeType;

    private StoreStatus status;

  
    private StoreContact contact;
}
