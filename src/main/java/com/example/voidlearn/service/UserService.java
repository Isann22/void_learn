package com.example.voidlearn.service;


import com.example.voidlearn.dto.CreateUserAdminRequest;
import com.example.voidlearn.dto.UserRegisterRequest;
import com.example.voidlearn.model.User;

import java.util.List;

public interface UserService {
    User register(UserRegisterRequest request);
    List<User> getAlluser();
    User updateUser(CreateUserAdminRequest request,String userId);
    User createAdminUser(CreateUserAdminRequest request);
    void deleteUser(String id);
}
