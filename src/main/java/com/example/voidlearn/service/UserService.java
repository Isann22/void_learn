package com.example.voidlearn.service;


import com.example.voidlearn.dto.UserRegisterRequest;
import com.example.voidlearn.model.User;

public interface UserService {
    User register(UserRegisterRequest request);
}
