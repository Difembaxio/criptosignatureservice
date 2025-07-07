package ru.difembaxio.exception;

import ru.difembaxio.userservice.model.Role;

public class UserChangeRoleException extends RuntimeException {

    public UserChangeRoleException(String message) {
        super(message);
    }
}
