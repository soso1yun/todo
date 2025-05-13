package com.llm.todo.common.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DataNotFoundException extends RuntimeException{

    private final String code;

    private final String message;

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
