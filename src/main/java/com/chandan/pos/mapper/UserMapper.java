package com.chandan.pos.mapper;

import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.UserDto;

public class UserMapper {

    public static UserDto toDTO(User savedUser){
        UserDto userDto = new UserDto();
        
        userDto.setId(savedUser.getId());
        userDto.setEmail(savedUser.getEmail());
        userDto.setRole(savedUser.getRole());
        userDto.setCreatedAt(savedUser.getCreatedAt());
        userDto.setUpdatedAt(savedUser.getUpdatedAt());
        userDto.setLastLogin(savedUser.getLastLogin());
        userDto.setPhone(savedUser.getPhone());
        
        return userDto;

    }
    
}
