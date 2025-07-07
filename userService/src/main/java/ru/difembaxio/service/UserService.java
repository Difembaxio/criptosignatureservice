package ru.difembaxio.service;

import java.util.List;
import ru.difembaxio.dto.PasswordChangeDto;
import ru.difembaxio.dto.RoleChangeDto;
import ru.difembaxio.dto.UserCreateDto;
import ru.difembaxio.dto.UserDto;



public interface UserService {

    UserDto registerUser (UserCreateDto userCreateDto);

    RoleChangeDto changeUserRole(RoleChangeDto roleChangeDto);
    UserDto updateUser(String username,UserDto userDto);
    PasswordChangeDto changeUserPassword(PasswordChangeDto passwordChangeDto);

    List<UserDto> getAllUser();
    void deleteUser(String username);
}