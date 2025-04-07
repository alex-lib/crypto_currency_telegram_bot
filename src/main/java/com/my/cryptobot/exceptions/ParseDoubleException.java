package com.my.cryptobot.exceptions;

public class ParseDoubleException extends RuntimeException {
    public ParseDoubleException(String message) {
        super("Переданное значение не является числом");
    }


}
