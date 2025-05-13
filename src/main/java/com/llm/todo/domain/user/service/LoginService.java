package com.llm.todo.domain.user.service;

import com.llm.todo.common.entity.ResultResponse;
import com.llm.todo.common.enums.ResultMessage;
import com.llm.todo.common.exception.DataNotFoundException;
import com.llm.todo.common.exception.ErrorCode;
import com.llm.todo.common.security.util.JwtUtil;
import com.llm.todo.domain.user.UserEntity;
import com.llm.todo.domain.user.dto.LoginRequest;
import com.llm.todo.domain.user.dto.UserDTO;
import com.llm.todo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;


    @Transactional(readOnly = true)
    public ResultResponse login(LoginRequest request) {

        UserEntity user = userRepository.findByUserId(request.getUserId());

        if (user == null || user.getId() < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResultResponse.builder()
                    .message(ResultMessage.INVALID_LOGIN_DATA.getMessage())
                    .success(false)
                    .build();
        }

        return ResultResponse.builder()
                .message(jwtUtil.createAccessToken(new UserDTO().from(user)))
                .build();
    }

}
