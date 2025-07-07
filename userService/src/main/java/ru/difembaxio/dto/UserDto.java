package ru.difembaxio.dto;

import jakarta.validation.constraints.Email;
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
public class UserDto {
    private Long id;
    @Size(min = 3, max = 15, message = "Логин должен содержать не менее {min} и не более {max} символов{}")
    private String username;
    @Email(message = "Неверный формат email")
    private String email;
    private String firstname;
    private String lastname;
    @Pattern(regexp = "^\\+79\\d{9}$", message = "Телефон должен содержать 12 символов и начинаться с +79")
    private String phone;
    private String role;
}