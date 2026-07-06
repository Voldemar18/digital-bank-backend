package com.digitalbank.user.security.service;

import com.digitalbank.user.security.details.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.digitalbank.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String login) {
        return userRepository.findByEmail(login)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с email: " + login));
    }
}