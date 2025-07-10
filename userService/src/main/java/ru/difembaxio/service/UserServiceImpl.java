package ru.difembaxio.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.difembaxio.dto.PasswordChangeDto;
import ru.difembaxio.dto.RoleChangeDto;
import ru.difembaxio.dto.UserCreateDto;
import ru.difembaxio.dto.UserDto;
import ru.difembaxio.dto.UserMapper;
import ru.difembaxio.dto.UserRabbitDto;
import ru.difembaxio.exception.InvalidPasswordException;
import ru.difembaxio.exception.UserChangeRoleException;
import ru.difembaxio.exception.UserExistsByEmailException;
import ru.difembaxio.exception.UserExistsByUsernameException;
import ru.difembaxio.exception.UserNotFoundException;
import ru.difembaxio.model.Role;
import ru.difembaxio.model.User;
import ru.difembaxio.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUser() {
        log.info("Запрос на получение списка пользователей");
        return userMapper.toListDto(userRepository.findAll());
    }

    @Override
    public UserDto registerUser(UserCreateDto userCreateDto) {
        log.info("Запрос на создание пользователя");
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new UserExistsByUsernameException(
                String.format("Пользователь с username %s уже существует",
                    userCreateDto.getUsername()));
        }
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new UserExistsByEmailException(
                String.format("Пользователь с email %s уже существует", userCreateDto.getEmail()));
        }
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        if (!userCreateDto.getPassword().matches(passwordRegex)) {
            throw new InvalidPasswordException(
                "Пароль должен содержать не менее 8 символов, включать как минимум одну"
                    + "заглавную и строчную букву, цифру и специальный символ.");
        }
        User userFromDb = userMapper.toUser(userCreateDto);
        userFromDb.setRole(Role.USER);
        log.info("Пользователь с username: {} успешно зарегистрирован",
            userCreateDto.getUsername());

        UserRabbitDto userRabbitDto = userMapper.toUserRabbitDto(userFromDb);
        rabbitTemplate.convertAndSend("myExchange","routingKeyQueue1",userRabbitDto);
        return userMapper.toDto(userRepository.save(userFromDb));
    }

    @Override
    public RoleChangeDto changeUserRole(RoleChangeDto roleChangeDto) {
        log.info("Смена роли пользователя на: {}", roleChangeDto.getUsername());
        User userFromDb = userRepository.findUserByUsernameIgnoreCase(roleChangeDto.getUsername())
            .orElseThrow(() -> new UserNotFoundException
                (String.format("Пользователь с username %s не найден",
                    roleChangeDto.getUsername())));
        String roleFromRequest = roleChangeDto.getRole().toUpperCase();
        if (roleFromRequest.equals("ADMIN") || roleFromRequest.equals("USER")) {
            userFromDb.setRole(Role.valueOf(roleFromRequest));
        } else {
            throw new UserChangeRoleException("Переданная роль не существует");
        }
        log.info("Смена роли пользователя с username: {} успешно произведена",
            roleChangeDto.getUsername());
        return userMapper.toRoleChangeDto(userRepository.save(userFromDb));
    }


    @Override
    public UserDto updateUser(String username, UserDto userDto) {
        log.info("Обновление пользователя с username: {}", username);
        User userFromDb = userRepository.findUserByUsernameIgnoreCase(username)
            .orElseThrow(() -> new UserNotFoundException(
                String.format("Пользователь с username %s не найден", username)));

        if (Objects.nonNull(userDto.getUsername()) && !userDto.getUsername()
            .equals(userFromDb.getUsername())) {
            if (userRepository.existsByUsername(userDto.getUsername())) {
                throw new UserExistsByUsernameException(
                    String.format("Пользователь с username %s уже существует", username));
            }
            userFromDb.setUsername(userDto.getUsername());
        }

        if (Objects.nonNull(userDto.getEmail()) && !userDto.getEmail()
            .equals(userFromDb.getEmail())) {
            userFromDb.setEmail(userDto.getEmail());
        }

        if (Objects.nonNull(userDto.getFirstname()) && !userDto.getFirstname()
            .equals(userFromDb.getFirstname())) {
            userFromDb.setFirstname(userDto.getFirstname());
        }

        if (Objects.nonNull(userDto.getLastname()) && !userDto.getLastname()
            .equals(userFromDb.getLastname())) {
            userFromDb.setLastname(userDto.getLastname());
        }

        if (Objects.nonNull(userDto.getPhone()) && !userDto.getPhone()
            .equals(userFromDb.getPhone())) {
            userFromDb.setPhone(userDto.getPhone());
        }
        log.info("Пользователь с username: {} успешно обновлен", username);
        return userMapper.toDto(userRepository.save(userFromDb));
    }

    @Override
    public PasswordChangeDto changeUserPassword(PasswordChangeDto passwordChangeDto) {
        log.info("Смена пароля пользователя с username: {}", passwordChangeDto.getUsername());
        User userFromDb = userRepository.findUserByUsernameIgnoreCase(
                passwordChangeDto.getUsername())
            .orElseThrow(() -> new UserNotFoundException
                (String.format("Пользователь с username %s не найден",
                    passwordChangeDto.getUsername())));
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        if (!passwordChangeDto.getPassword().matches(passwordRegex)) {
            throw new InvalidPasswordException(
                "Пароль должен содержать не менее 8 символов, включать как минимум одну"
                    + "заглавную и строчную букву, цифру и специальный символ.");
        }
        userFromDb.setPassword(passwordChangeDto.getPassword());
        log.info("Пароль пользователя с username: {} успешно изменен",
            passwordChangeDto.getUsername());
        return userMapper.toPasswordChangeDto(userRepository.save(userFromDb));
    }

    @Override
    public void deleteUser(String username) {
        log.info("Запрос на удаление пользователя");
        User userFromDb = userRepository.findUserByUsernameIgnoreCase(username)
            .orElseThrow(() -> new UserNotFoundException(
                String.format("Пользователь с username %s не найден", username)));
        userRepository.delete(userFromDb);
        log.info("Удаление пользователя с username: {} выполнено успешно", username);
    }
}


