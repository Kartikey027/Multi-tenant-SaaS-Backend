package com.kartikey.saas.common.exception;


public class ForbiddenOperationException extends DomainException{
    public ForbiddenOperationException(String msg){
        super(msg);
    }
}
