package com.llm.todo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DEFAULT_DUPLICATE("중복입니다.","DATA_DUPLICATE"),
    DEFAULT_DATABASE("서버 내부 오류.","INTERNAL_SERVER_ERROR"),
    DEFAULT_NOT_FOUND("데이터가 없습니다.", "DATA_NOT_FOUND"),
    VALIDATION_ERROR("데이터 형식을 확인해주세요.", "VALIDATION_ERROR"),
    NO_PERMISSION("권한이 없습니다.", "NO_PERMISSION")
    ;

    private final String message;
    private final String code;
}
