package ru.difembaxio.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleChangeDto {
    @NotNull(message = "Логин не может быть пустым")
    @Size(min = 3, max = 15, message = "Логин должен содержать не менее {min} и не более {max} символов{}")
    private String username;
    @NotNull(message = "Роль не может быть пустой")
    private String role;
}
