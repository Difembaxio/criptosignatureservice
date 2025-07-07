package ru.difembaxio.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.difembaxio.dto.PasswordChangeDto;
import ru.difembaxio.dto.RoleChangeDto;
import ru.difembaxio.dto.UserCreateDto;
import ru.difembaxio.dto.UserDto;
import ru.difembaxio.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    public UserDto registerUser(@Valid @RequestBody UserCreateDto userCreateDto){
        return userService.registerUser(userCreateDto);
    }

    @PatchMapping("/change/role")
    public RoleChangeDto changeUserRole(@Valid @RequestBody RoleChangeDto roleChangeDto){
        return userService.changeUserRole(roleChangeDto);
    }

    @PatchMapping("/{username}")
    public UserDto updateUser(@Valid @PathVariable String username,
        @Valid @RequestBody UserDto userDto){
        return userService.updateUser(username,userDto);
    }

    @PatchMapping("/change/password")
    public PasswordChangeDto changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto){
        return userService.changeUserPassword(passwordChangeDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.getAllUser();
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@Valid @PathVariable String username){
        userService.deleteUser(username);
    }
}
