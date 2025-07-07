package ru.difembaxio.exception;

public class UserExistsByEmailException extends RuntimeException {

    public UserExistsByEmailException(String message) {
        super(message);
    }

}