package com.llm.todo.domain.user.dto;

import com.llm.todo.domain.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private long id;

    private String userName;

    private String userId;


    public UserResponse from(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userId(user.getUserId())
                .build();
    }

}
