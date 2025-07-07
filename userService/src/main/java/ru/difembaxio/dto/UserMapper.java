package ru.difembaxio.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.difembaxio.model.User;


@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toUser (UserCreateDto userCreateDto){
        return modelMapper.map(userCreateDto, User.class);
    }

    public UserDto toDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
    public PasswordChangeDto toPasswordChangeDto(User user){
        return modelMapper.map(user, PasswordChangeDto.class);
    }

    public List<UserDto> toListDto(List<User> users){
        return users.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public RoleChangeDto toRoleChangeDto(User user){
        return modelMapper.map(user, RoleChangeDto.class);
    }
}