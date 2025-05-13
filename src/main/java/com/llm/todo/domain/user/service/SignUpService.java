package com.llm.todo.domain.user.service;

import com.llm.todo.common.entity.ResultResponse;
import com.llm.todo.common.enums.ResultMessage;
import com.llm.todo.common.exception.DatabaseException;
import com.llm.todo.common.exception.DuplicateException;
import com.llm.todo.common.exception.ErrorCode;
import com.llm.todo.domain.user.dto.SignUpRequest;
import com.llm.todo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public ResultResponse signup(SignUpRequest request) {

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateException(ErrorCode.DEFAULT_DUPLICATE);
        }


        if (userRepository.save(request.to(passwordEncoder.encode(request.getPassword()))).getId() < 1) {
            throw new DatabaseException(ErrorCode.DEFAULT_DATABASE);
        }

        return ResultResponse.builder()
                .message(ResultMessage.SIGNUP_SUCCESS.getMessage())
                .build();
    }


}
