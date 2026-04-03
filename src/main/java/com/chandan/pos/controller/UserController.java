package com.chandan.pos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.mapper.UserMapper;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.UserDto;
import com.chandan.pos.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(
        @RequestHeader("Authorization") String jwt
    ) throws UserException {
       User user=userService.getUserFromJwtToken(jwt);
       return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
        @RequestHeader("Authorization") String jwt,
        @PathVariable Long id
    ) throws UserException {
       User user=userService.getUserById(id);
       return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    
}