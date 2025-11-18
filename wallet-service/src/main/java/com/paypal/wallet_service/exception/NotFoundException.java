package com.paypal.wallet_service.exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }
}
