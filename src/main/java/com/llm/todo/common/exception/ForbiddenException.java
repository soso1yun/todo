package com.llm.todo.common.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ForbiddenException extends RuntimeException{

    private final String code;

    private final String message;

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
