package org.example.exception;

public class FailedAttackException extends RuntimeException {

    public FailedAttackException(String message) {
        super(message);
    }
}
