package com.llm.todo.common.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
