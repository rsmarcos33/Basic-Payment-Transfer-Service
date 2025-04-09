package com.test.paymenttransfer.exception;

public class TransferToSameAccountException extends RuntimeException {

    public TransferToSameAccountException(String message) {
        super(message);
    }
}
