package com.digitalbank.user.service;

import com.digitalbank.user.model.entity.RefreshToken;
import com.digitalbank.user.model.entity.User;
import com.digitalbank.user.repository.RefreshTokenRepository;
import com.digitalbank.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public RefreshToken create (String login) {
        User user = userRepository.findByEmail(login).orElseThrow(() -> new UsernameNotFoundException("Не удалось найти пользователя!"));

        String refreshToken = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(30, ChronoUnit.DAYS);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryDate(expiryDate)
                .build();

        return refreshTokenRepository.save(token);
    }

    @Transactional
    public void delete (String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new BadCredentialsException("Токен не найден!"));
        refreshTokenRepository.delete(token);
    }
}