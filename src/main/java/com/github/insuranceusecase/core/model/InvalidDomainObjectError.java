package com.github.insuranceusecase.core.model;

public class InvalidDomainObjectError extends RuntimeException {
    public InvalidDomainObjectError() {
    }

    public InvalidDomainObjectError(String message) {
        super(message);
    }
}
