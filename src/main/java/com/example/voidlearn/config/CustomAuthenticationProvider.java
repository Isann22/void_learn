package com.example.voidlearn.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final SecurityBeansConfig passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.debug("=== AUTHENTICATION ATTEMPT ===");
        log.debug("Email: {}", email);
        log.debug("Password provided: {}", password);

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            /*
            log.debug("UserDetails loaded successfully");
            log.debug("Username from UserDetails: {}", userDetails.getUsername());
            log.debug("Authorities: {}", userDetails.getAuthorities());
            log.debug("Enabled: {}", userDetails.isEnabled());
            log.debug("Account non expired: {}", userDetails.isAccountNonExpired());
            log.debug("Account non locked: {}", userDetails.isAccountNonLocked());
            log.debug("Credentials non expired: {}", userDetails.isCredentialsNonExpired());
             */


            boolean passwordMatches = passwordEncoder.passwordEncoder().matches(password, userDetails.getPassword());
            log.debug("Password matches: {}", passwordMatches);
            log.debug("Stored password hash: {}", userDetails.getPassword());

            if (!passwordMatches) {
//                log.error("Password does not match for user: {}", email);
                throw new BadCredentialsException("Invalid credentials");
            }

            // Check account status
            if (!userDetails.isEnabled()) {
//                log.error("User account is disabled: {}", email);
                throw new BadCredentialsException("Account is disabled");
            }

            if (!userDetails.isAccountNonExpired()) {
//                log.error("User account is expired: {}", email);
                throw new BadCredentialsException("Account is expired");
            }

            if (!userDetails.isAccountNonLocked()) {
//                log.error("User account is locked: {}", email);
                throw new BadCredentialsException("Account is locked");
            }

            if (!userDetails.isCredentialsNonExpired()) {
//                log.error("User credentials are expired: {}", email);
                throw new BadCredentialsException("Credentials are expired");
            }

            log.debug("Authentication successful for user: {}", email);
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    password,
                    userDetails.getAuthorities()
            );

        } catch (Exception e) {
            log.error("Authentication failed for user {}: {}", email, e.getMessage(), e);
            throw new BadCredentialsException("Authentication failed", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}