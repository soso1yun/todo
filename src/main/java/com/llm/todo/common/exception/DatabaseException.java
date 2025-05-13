package com.llm.todo.common.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DatabaseException extends RuntimeException{

    private final String code;

    private final String message;

    public DatabaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
