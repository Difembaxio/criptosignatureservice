package ru.difembaxio.exception;

import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
