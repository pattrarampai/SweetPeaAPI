package com.sweetpeatime.sweetpeatime.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(value = {CommonException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonExceptionResponse handlerException(CommonException ex, WebRequest request){
        log.error("{}", ex);
        return new CommonExceptionResponse(ex.getMessage());
    }
}
