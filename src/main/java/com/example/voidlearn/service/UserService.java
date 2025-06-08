package com.example.voidlearn.service;



import com.example.voidlearn.dao.UserProjection;
import com.example.voidlearn.dto.UserDto;
import com.example.voidlearn.model.Course;
import com.example.voidlearn.model.User;

import java.util.List;

public interface UserService {
    User register(UserDto request);
    User createAdmin(UserDto request);
    User updateUser(UserDto request, String userId);
    void deleteUser(String id);
    long countRoleUser();
    List<UserProjection> getUserData();
}
