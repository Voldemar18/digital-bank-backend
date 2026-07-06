package com.digitalbank.user.service;

import com.digitalbank.user.model.dto.LoginRq;
import com.digitalbank.user.model.dto.TokenMappingRs;
import com.digitalbank.user.model.entity.RefreshToken;
import com.digitalbank.user.repository.RefreshTokenRepository;
import com.digitalbank.user.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    public TokenMappingRs login (LoginRq dto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getLogin(),
                            dto.getPassword())
            );
        } catch (BadCredentialsException exception) {
            log.warn("Ошибка входа: неверный логин или пароль для пользователя: {}", dto.getLogin());
            throw new BadCredentialsException("Неверный логин или пароль");
        }

        String accessToken = jwtUtil.generateAccessToken(dto.getLogin());
        String refreshToken = jwtUtil.generateRefreshToken(dto.getLogin());

        return new TokenMappingRs(accessToken, refreshToken);
    }

    public TokenMappingRs refresh (String refreshToken) {
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            throw new BadCredentialsException("Невалидный токен!");
        }

        String login = jwtUtil.extractUsername(refreshToken);

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new BadCredentialsException("Не удалось найти токен для обновления сессии!"));

        if (!token.getUser().getEmail().equals(login)) {
            throw new BadCredentialsException("Токен не принадлежит пользователю!");
        }

        String newAccessToken = jwtUtil.generateAccessToken(login);
        refreshTokenRepository.delete(token);
        RefreshToken refreshToken1 = refreshTokenService.create(login);

        return new TokenMappingRs(newAccessToken, refreshToken1.getToken());
    }
}