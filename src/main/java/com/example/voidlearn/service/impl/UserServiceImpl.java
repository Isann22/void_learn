package com.example.voidlearn.service.impl;

import com.example.voidlearn.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl {

    private UserRepository userRepository;


}
