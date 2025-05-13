package com.llm.todo.domain.todo;

import com.llm.todo.common.entity.ResultResponse;
import com.llm.todo.common.exception.DataNotFoundException;
import com.llm.todo.common.exception.ErrorCode;
import com.llm.todo.common.security.service.CustomUserDetails;
import com.llm.todo.domain.todo.dto.TodoRequest;
import com.llm.todo.domain.todo.dto.TodoSearchRequest;
import com.llm.todo.domain.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<ResultResponse> addTodos(@RequestBody @Valid TodoRequest todoRequest, @AuthenticationPrincipal CustomUserDetails userDTO) {
        return ResponseEntity.ok(todoService.addTodos(todoRequest,userDTO.getUserDTO().getId()));
    }

    @GetMapping("/todos")
    public ResponseEntity<ResultResponse> getTodos(@AuthenticationPrincipal CustomUserDetails userDTO) {
        return ResponseEntity.ok(todoService.getTodos(userDTO.getUserDTO().getId()));
    }


    @GetMapping("/todos/{id}")
    public ResponseEntity<ResultResponse> getTodosId(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDTO) {
        if (id < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }
        return ResponseEntity.ok(todoService.getTodos(id, userDTO.getUserDTO().getId()));
    }


    @PutMapping("/todos/{id}")
    public ResponseEntity<ResultResponse> updateTodos(@PathVariable Long id, @RequestBody @Valid TodoRequest todoRequest,
                                                      @AuthenticationPrincipal CustomUserDetails userDTO) {
        if (id < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }
        return ResponseEntity.ok(todoService.updateTodos(id, todoRequest, userDTO.getUserDTO().getId()));
    }


    @DeleteMapping("/todos/{id}")
    public ResponseEntity<ResultResponse> delTodos(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDTO) {
        if (id < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        return ResponseEntity.ok(todoService.delTodos(id, userDTO.getUserDTO().getId()));
    }

    @GetMapping("/todos/search")
    public ResponseEntity<ResultResponse> searchTodo(@Valid @RequestBody TodoSearchRequest todoSearchRequest, @AuthenticationPrincipal CustomUserDetails userDTO) {
        return ResponseEntity.ok(todoService.searchTodos(todoSearchRequest, userDTO.getUserDTO().getId()));
    }

}
