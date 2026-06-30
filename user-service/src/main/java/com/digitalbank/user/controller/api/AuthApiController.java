package com.digitalbank.user.controller.api;

import com.digitalbank.user.model.dto.CreateUserRq;
import com.digitalbank.user.model.dto.LoginRq;
import com.digitalbank.user.model.dto.TokenMappingRs;
import com.digitalbank.user.service.AuthService;
import com.digitalbank.user.service.RefreshTokenService;
import com.digitalbank.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> createUser (@Valid @RequestBody CreateUserRq createUserRq) {
        userService.create(createUserRq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginRq dto, HttpServletResponse response) {
        try {
            TokenMappingRs tokens = authService.login(dto, response);

            ResponseCookie accessCookie = createAuthCookie("accessToken", tokens.getAccessToken(), 900);
            ResponseCookie refreshToken = createAuthCookie("refreshToken", tokens.getRefreshToken(), 2592000);

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());

            return ResponseEntity.ok().body("Успешный вход!");
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль!");
        }
    }

    private ResponseCookie createAuthCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh (HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        try {
            TokenMappingRs tokens = authService.refresh(refreshToken);

            ResponseCookie accessCookie = createAuthCookie("accessToken", tokens.getAccessToken(), 900);
            ResponseCookie refreshCookie = createAuthCookie("refreshToken", tokens.getRefreshToken(), 2592000);

            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            return ResponseEntity.ok().build();
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenService.delete(cookie.getValue());
                    break;
                }
            }
        }

        ResponseCookie accessCookie = clearCookie("accessToken");
        ResponseCookie refreshCookie = clearCookie("refreshToken");

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().body("Вы успешно вышли из аккаунта!");
    }

    private ResponseCookie clearCookie(String name) {
        return ResponseCookie.from(name)
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();
    }
}