package com.chandan.pos.service;

import java.util.List;

import com.chandan.pos.exceptions.UserException;
import com.chandan.pos.modal.User;

public interface UserService {
     User getUserFromJwtToken(String token) throws UserException;
     User getCurrentUser() throws UserException;
     User getUserByEmail(String email) throws UserException;
     User getUserById(Long id) throws UserException;
     List<User> getAllUsers();   
}
