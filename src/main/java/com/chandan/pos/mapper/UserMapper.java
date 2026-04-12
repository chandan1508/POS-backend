package com.chandan.pos.mapper;

import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.UserDto;

public class UserMapper {

    public static UserDto toDTO(User savedUser){
        UserDto userDto = new UserDto();
        
        userDto.setId(savedUser.getId());
        userDto.setFullName(savedUser.getFullName());
        userDto.setEmail(savedUser.getEmail());
        userDto.setRole(savedUser.getRole());
        userDto.setCreatedAt(savedUser.getCreatedAt());
        userDto.setUpdatedAt(savedUser.getUpdatedAt());
        userDto.setLastLogin(savedUser.getLastLogin());
        userDto.setPhone(savedUser.getPhone());
        userDto.setBranchId(savedUser.getBranch()!=null? savedUser.getBranch().getId():null);
        userDto.setStoreId(savedUser.getStore()!=null? savedUser.getStore().getId():null);
        
        return userDto;

    }

    public static User toEntity(UserDto userDto){
        User createUser = new User();
        
        createUser.setFullName(userDto.getFullName());
        createUser.setEmail(userDto.getEmail());
        createUser.setRole(userDto.getRole());
        createUser.setCreatedAt(userDto.getCreatedAt());
        createUser.setUpdatedAt(userDto.getUpdatedAt());
        createUser.setLastLogin(userDto.getLastLogin());
        createUser.setPhone(userDto.getPhone());
        createUser.setPassword(userDto.getPassword());
        
        return createUser;

    }
    
}
