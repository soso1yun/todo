package com.llm.todo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultMessage {
    SIGNUP_SUCCESS("회원가입에 성공하였습니다."),
    INVALID_LOGIN_DATA("로그인 정보가 일치하지 않습니다."),
    LOGIN_AGAIN("수정이 완료되었습니다. 다시 로그인해주세요."),
    EDIT_COMPLETED("수정이 완료되었습니다."),
    ADD_COMPLETED("등록이 완료되었습니다."),
    DELETE_COMPLETED("삭제가 완료되었습니다.")
    ;

    private final String message;
}
