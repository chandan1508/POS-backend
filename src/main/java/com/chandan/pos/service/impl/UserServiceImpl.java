package com.chandan.pos.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.chandan.pos.configuration.JwtProvider;
import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.modal.User;
import com.chandan.pos.repository.UserRepository;
import com.chandan.pos.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User getUserFromJwtToken(String token) throws UserException {
       String email = jwtProvider.getEmailFromToken(token);
       User user = userRepository.findByEmail(email);
       if(user==null){
        throw new UserException("Invalid token");
       }
       return user;
    }

    @Override
    public User getCurrentUser() throws UserException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        if(user==null){
        throw new UserException("user not found");
       }
       return user;
    }

    @Override
    public User getUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if(user==null){
        throw new UserException("user not found");
       }
       return user;
    }

    @Override
    public User getUserById(Long id) throws UserException {
        return userRepository.findById(id).orElseThrow(
            ()-> new UserException("user not found")
        );
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
