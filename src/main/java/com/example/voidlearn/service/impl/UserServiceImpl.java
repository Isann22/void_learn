package com.example.voidlearn.service.impl;

//import com.example.voidlearn.config.SecurityBeansConfig;
import com.example.voidlearn.dto.UserRegisterRequest;
import com.example.voidlearn.model.Role;
import com.example.voidlearn.model.User;
import com.example.voidlearn.repo.UserRepository;
import com.example.voidlearn.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional
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

    @Transactional
    @Override
    public User register(UserRegisterRequest request) {
        log.debug("Registering new user with email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        log.debug("User registered successfully with id: {}", savedUser.getId());
        return savedUser;
    }
}

