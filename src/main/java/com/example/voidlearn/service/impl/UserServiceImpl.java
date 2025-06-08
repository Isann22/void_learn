package com.example.voidlearn.service.impl;


import com.example.voidlearn.dao.UserProjection;
import com.example.voidlearn.dto.UserDto;
import com.example.voidlearn.model.Role;
import com.example.voidlearn.model.User;
import com.example.voidlearn.repo.UserRepository;
import com.example.voidlearn.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            log.debug("Attempting to load user by email: {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        String errorMessage = String.format("User with email '%s' not found", email);
                        log.error(errorMessage);
                        return new UsernameNotFoundException(errorMessage);
                    });
            log.error("Encoded password in DB: {}", user.getPassword());
            log.error("User found: {} with role: {}", user.getEmail(), user.getRole());
            return user;
        }


    @Override
    public User register(UserDto request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }


    @Override
    public User createAdmin(UserDto request) {
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.ADMIN);
        return userRepository.save(newUser);
    }

    @Override
    public List<UserProjection> getUserData() {
        return userRepository.getUserData();
    }


    @Override
    public User updateUser(UserDto request, String userId) {
        User userUpdate  = userRepository.findById(userId).get();
        userUpdate.setName(request.getName());
        userUpdate.setUsername(request.getUsername());
        userUpdate.setEmail(request.getEmail());
        userUpdate.setRole(Role.ADMIN);
        userUpdate.setModifiedDate(LocalDateTime.now());
        return userRepository.save(userUpdate);
    }



    @Override
    public long countRoleUser() {
        return userRepository.countByRoleUser();
    }


    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
        }
}

