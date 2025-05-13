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
public class UserDTO {

    private long id;

    private String userName;

    private String userId;

    private String password;

    @Builder.Default
    private String role = "ROLE_USER";

    public UserDTO from(UserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userId(user.getUserId())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public UserEntity to() {
        return UserEntity.builder()
                .userId(userId)
                .userName(userName)
                .password(password)
                .role(role)
                .build();
    }

}
