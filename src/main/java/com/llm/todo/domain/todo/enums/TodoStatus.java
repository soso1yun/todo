package com.llm.todo.domain.todo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TodoStatus {
    PENDING("PENDING", "대기"),
    EMERGENCY("EMERGENCY", "긴급"),
    IN_PROGRESS("IN_PROGRESS", "진행중"),
    DONE("DONE", "완료");

    private final String value;
    private final String text;


    public static TodoStatus fromText(String text) {
        for (TodoStatus status : TodoStatus.values()){
            if (status.getText().equals(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("INVALID VALUE : " + text);
    }

}
