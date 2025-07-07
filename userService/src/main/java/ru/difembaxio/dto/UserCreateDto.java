package ru.difembaxio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @NotNull(message = "Логин не может быть пустым")
    @Size(min = 3, max = 15, message = "Логин должен содержать не менее {min} и не более {max} символов{}")
    private String username;

    @NotNull
    @Email(message = "Неверный формат email")
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    @Pattern(regexp = "^\\+79\\d{9}$", message = "Телефон должен содержать 12 символов и начинаться с +79")
    private String phone;

}