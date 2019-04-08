package com.gateway.exception;

import feign.FeignException;

public class CustomFeignException extends FeignException {

    public CustomFeignException(String message) {
        super(message);
    }
}
