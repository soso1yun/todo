package com.llm.todo.domain.user.dto;

import com.llm.todo.domain.user.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "[A-Za-z]+(\\s[A-Za-z]+)*|[가-힣]+", message = "이름은 한글 또는 영문으로만 입력해주세요.")
    private String userName;

    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9])[a-zA-Z0-9]{3,16}$", message = "3자 이상 16자 이하, 영어 또는 숫자로 구성해주세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?:(?=.*[a-z])(?=.*[A-Z])|(?=.*[a-z])(?=.*\\d)|(?=.*[A-Z])(?=.*\\d)).{6,10}$", message = "비밀번호는 6~10자 영문 소문자, 대문자, 숫자 중 최소 두 가지 이상 조합을 사용하세요.")
    @Size(min = 6, max = 10, message = "비밀번호는 최소 6자 이상 10자 이하입니다.")
    private String password;

    @Builder.Default
    private String role = "ROLE_USER";


    public UserEntity to(String setPassword) {
        return UserEntity.builder()
                .userId(userId)
                .userName(userName)
                .password(setPassword)
                .role(role)
                .build();
    }

}
