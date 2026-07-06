package com.digitalbank.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRq {
    @NotBlank(message = "Имя обязательно!")
    private String name;
    @NotBlank(message = "Почта обязательна")
    private String email;
    @NotBlank(message = "Пароль обязателен!")
    private String password;
}