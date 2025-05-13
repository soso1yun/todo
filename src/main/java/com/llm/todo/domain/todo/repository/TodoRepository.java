package com.llm.todo.domain.todo.repository;

import com.llm.todo.domain.todo.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    TodoEntity findById(long id);

    TodoEntity findByIdAndUserId(long id, long userId);

    boolean existsById(long id);

    boolean existsByIdAndUserId(long id, long userId);

    void deleteById(long id);

    List<TodoEntity> findAllByUserId(long userId);

    List<TodoEntity> findAllByUserIdAndDueDateBetween(long userid, LocalDate start, LocalDate end);
}
