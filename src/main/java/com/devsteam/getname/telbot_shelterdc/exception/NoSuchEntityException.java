package com.devsteam.getname.telbot_shelterdc.exception;

import java.io.IOException;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException(String message) {
        super(message);
    }
}
