package com.digitalbank.user.service;

import com.digitalbank.user.mapper.UserMapper;
import com.digitalbank.user.model.dto.CreateUserRq;
import com.digitalbank.user.model.entity.User;
import com.digitalbank.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void create (CreateUserRq createUserRq) {
        User user = userMapper.toEntity(createUserRq);
        userRepository.save(user);
    }
}