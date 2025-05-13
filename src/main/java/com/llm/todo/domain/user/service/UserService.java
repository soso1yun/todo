package com.llm.todo.domain.user.service;

import com.llm.todo.common.entity.ResultResponse;
import com.llm.todo.common.enums.ResultMessage;
import com.llm.todo.common.exception.DataNotFoundException;
import com.llm.todo.common.exception.ErrorCode;
import com.llm.todo.domain.user.UserEntity;
import com.llm.todo.domain.user.dto.UserRequest;
import com.llm.todo.domain.user.dto.UserResponse;
import com.llm.todo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public ResultResponse getUserInfo(long id) {

        UserResponse user = new UserResponse().from(userRepository.findById(id));

        return ResultResponse.builder()
                .success(true)
                .data(Map.of("userInfo", user))
                .build();
    }

    @Transactional
    public ResultResponse updateUserInfo(UserRequest request, long id) {
        UserEntity user = userRepository.findById(id);
        boolean passwordChanged = false;

        if (request.getPassword().isEmpty() && request.getUserName().isEmpty()) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        if (!request.getPassword().isEmpty()
                && !Pattern.matches("^(?:(?=.*[a-z])(?=.*[A-Z])|(?=.*[a-z])(?=.*\\d)|(?=.*[A-Z])(?=.*\\d)).{6,10}$",
                request.getPassword())) {
            throw new DataNotFoundException(ErrorCode.VALIDATION_ERROR);
        }else {
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                passwordChanged = true;
            }
        }

        if (!request.getUserName().isEmpty()
                && !Pattern.matches("[A-Za-z]+(\\s[A-Za-z]+)*|[가-힣]+", request.getUserName())) {
            throw new DataNotFoundException(ErrorCode.VALIDATION_ERROR);
        }else {
            user.setUserName(request.getUserName());
        }

        return ResultResponse.builder()
                .success(true)
                .message(passwordChanged ? ResultMessage.LOGIN_AGAIN.getMessage() :
                        ResultMessage.EDIT_COMPLETED.getMessage()
                )
                .build();
    }

    @Transactional
    public ResultResponse deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        userRepository.deleteById(id);

        return ResultResponse.builder()
                .success(true)
                .message(ResultMessage.DELETE_COMPLETED.getMessage())
                .build();
    }

}
