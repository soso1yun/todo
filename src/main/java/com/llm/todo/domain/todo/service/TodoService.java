package com.llm.todo.domain.todo.service;

import com.llm.todo.common.entity.ResultResponse;
import com.llm.todo.common.enums.ResultMessage;
import com.llm.todo.common.exception.DataNotFoundException;
import com.llm.todo.common.exception.ErrorCode;
import com.llm.todo.common.exception.ForbiddenException;
import com.llm.todo.domain.todo.TodoEntity;
import com.llm.todo.domain.todo.dto.TodoRequest;
import com.llm.todo.domain.todo.dto.TodoResponse;
import com.llm.todo.domain.todo.dto.TodoSearchRequest;
import com.llm.todo.domain.todo.enums.TodoStatus;
import com.llm.todo.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;


    @Transactional
    public ResultResponse addTodos(TodoRequest todoRequest, long userId) {
        if (todoRepository.save(todoRequest.to(userId)).getId() < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_DATABASE);
        }

        return ResultResponse.builder()
                .success(true)
                .message(ResultMessage.ADD_COMPLETED.getMessage())
                .build();
    }

    @Transactional(readOnly = true)
    public ResultResponse getTodos(long userId) {

        List<TodoEntity> todo = todoRepository.findAllByUserId(userId);

        if (todo.isEmpty()) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        List<TodoResponse> result = todo.stream()
                .map(TodoResponse::from)
                .toList();

        return ResultResponse.builder()
                .success(true)
                .data(Map.of("todoList", result))
                .build();
    }

    @Transactional(readOnly = true)
    public ResultResponse getTodos(long id, long userId) {
        if (!todoRepository.existsById(id)) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        TodoEntity todoEntity = todoRepository.findByIdAndUserId(id, userId);

        if (todoEntity.getId() < 0) {
            throw new ForbiddenException(ErrorCode.NO_PERMISSION);
        }

        return ResultResponse.builder()
                .success(true)
                .data(Map.of("todoData", TodoResponse.from(todoEntity)))
                .build();
    }

    @Transactional(readOnly = true)
    public ResultResponse searchTodos(TodoSearchRequest search, long userId) {

        LocalDate startDueDate = StringUtils.isBlank(search.getStartDueDate()) ?  LocalDate.now() : LocalDate.parse(search.getStartDueDate());
        LocalDate endDueDate = StringUtils.isBlank(search.getEndDueDate()) ?  LocalDate.now() : LocalDate.parse(search.getEndDueDate());

        List<TodoEntity> todoEntities = todoRepository.findAllByUserIdAndDueDateBetween(userId, startDueDate, endDueDate);

        if (StringUtils.isNotBlank(search.getSearchText())) {
            todoEntities = todoEntities.stream()
                    .filter(s -> s.getTitle().toLowerCase().contains(search.getSearchText().toLowerCase()) ||
                            s.getDescription().toLowerCase().contains(search.getSearchText().toLowerCase())
                    )
                    .toList();
        }

        if (StringUtils.isNotBlank(search.getStatus())) {
            todoEntities = todoEntities.stream()
                    .filter(s -> s.getStatus() == TodoStatus.fromText(search.getSearchText()))
                    .toList();
        }

        return ResultResponse.builder()
                .success(true)
                .data(Map.of("todoList", todoEntities))
                .build();
    }

    @Transactional
    public ResultResponse updateTodos(long id, TodoRequest request, long userId) {
        if (!todoRepository.existsById(id)) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        TodoEntity todoEntity = todoRepository.findById(id);

        if (userId != todoEntity.getUserId()) {
            throw new ForbiddenException(ErrorCode.NO_PERMISSION);
        }

        todoEntity.setDescription(request.getDescription());
        todoEntity.setTitle(request.getTitle());
        todoEntity.setDueDate(LocalDate.parse(request.getDueDate()));
        todoEntity.setStatus(TodoStatus.fromText(request.getStatus()));

        return ResultResponse.builder()
                .success(true)
                .message(ResultMessage.EDIT_COMPLETED.getMessage())
                .build();
    }

    @Transactional
    public ResultResponse delTodos(long id, long userId) {
        if (!todoRepository.existsById(id)) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        if (!todoRepository.existsByIdAndUserId(id, userId)) {
            throw new ForbiddenException(ErrorCode.NO_PERMISSION);
        }

        todoRepository.deleteById(id);

        return ResultResponse.builder()
                .success(true)
                .message(ResultMessage.DELETE_COMPLETED.getMessage())
                .build();
    }


}
