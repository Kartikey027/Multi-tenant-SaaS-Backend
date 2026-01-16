package com.kartikey.saas.common.exception;

public abstract class DomainException extends RuntimeException{
    protected DomainException(String msg){
        super(msg);
    }
}
