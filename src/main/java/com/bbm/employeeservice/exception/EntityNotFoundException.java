package com.bbm.employeeservice.exception;

public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
