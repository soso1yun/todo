package com.llm.todo.domain.todo.dto;

import com.llm.todo.domain.todo.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse{

    private Long id;

    private String title;

    private String description;

    private String dueDate;

    private String status;

    public static TodoResponse from(TodoEntity todo){
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .dueDate(todo.getDescription())
                .status(todo.getStatus().getText())
                .build();

    }

}

