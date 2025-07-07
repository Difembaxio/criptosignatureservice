package ru.difembaxio.exception;

public class UserExistsByUsernameException extends RuntimeException {

    public UserExistsByUsernameException(String message) {
        super(message);
    }

}