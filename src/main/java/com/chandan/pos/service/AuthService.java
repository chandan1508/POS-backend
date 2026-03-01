package com.chandan.pos.service;

import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.payload.dto.UserDto;
import com.chandan.pos.payload.response.AuthResponse;

public interface AuthService {
   
    AuthResponse signup(UserDto userDto) throws UserException;
    AuthResponse login(UserDto userDto) throws UserException;
    
} 
