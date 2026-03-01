package com.chandan.pos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.payload.dto.UserDto;
import com.chandan.pos.payload.response.AuthResponse;
import com.chandan.pos.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(
        @RequestBody UserDto userDto
    ) throws UserException {
        return ResponseEntity.ok(
            authService.signup(userDto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
        @RequestBody UserDto userDto
    ) throws UserException {
        return ResponseEntity.ok(
            authService.login(userDto)
        );
    }
    
}
