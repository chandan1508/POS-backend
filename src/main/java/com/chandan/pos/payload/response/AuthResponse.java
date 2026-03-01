package com.chandan.pos.payload.response;

import com.chandan.pos.payload.dto.UserDto;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private UserDto userDto;
    
}
