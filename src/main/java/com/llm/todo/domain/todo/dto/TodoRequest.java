package com.llm.todo.domain.todo.dto;

import com.llm.todo.common.entity.BaseEntity;
import com.llm.todo.domain.todo.TodoEntity;
import com.llm.todo.domain.todo.enums.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest extends BaseEntity {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "날짜는 YYYY-MM-DD 형태로 넣어주세요.")
    private String dueDate;

    @NotBlank
    @Pattern(regexp = "^(대기|긴급|진행중|완료)$")
    private String status;

    public TodoEntity to(long userId){
        return TodoEntity.builder()
                .title(title)
                .userId(userId)
                .description(description)
                .dueDate(LocalDate.parse(dueDate))
                .status(TodoStatus.fromText(status))
                .build();
    }

}

