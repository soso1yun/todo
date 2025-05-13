package com.llm.todo.domain.user.repository;

import com.llm.todo.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);

    UserEntity findById(long id);

    boolean existsByUserId(String userId);

    boolean existsById(long id);

    void deleteById(long id);
}
