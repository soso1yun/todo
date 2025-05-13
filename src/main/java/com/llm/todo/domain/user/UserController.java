package com.llm.todo.domain.user;

import com.llm.todo.common.entity.ResultResponse;
import com.llm.todo.common.exception.DataNotFoundException;
import com.llm.todo.common.exception.ErrorCode;
import com.llm.todo.common.security.service.CustomUserDetails;
import com.llm.todo.domain.user.dto.LoginRequest;
import com.llm.todo.domain.user.dto.SignUpRequest;
import com.llm.todo.domain.user.dto.UserRequest;
import com.llm.todo.domain.user.service.LoginService;
import com.llm.todo.domain.user.service.SignUpService;
import com.llm.todo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final SignUpService signupService;

    private final LoginService loginService;

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<ResultResponse> signupUser(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(signupService.signup(request));
    }

    @PostMapping("/users/login")
    public ResponseEntity<ResultResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request));
    }

    @GetMapping("/users/me")
    public ResponseEntity<ResultResponse> getUser(@AuthenticationPrincipal CustomUserDetails userDTO) {
        if (userDTO == null || userDTO.getUserDTO().getId() < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }

        return ResponseEntity.ok(userService.getUserInfo(userDTO.getUserDTO().getId()));
    }

    @PutMapping("/users/me")
    public ResponseEntity<ResultResponse> updateUser(@Valid @RequestBody UserRequest request, @AuthenticationPrincipal CustomUserDetails userDTO) {
        if (userDTO == null || userDTO.getUserDTO().getId() < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }
        return ResponseEntity.ok(userService.updateUserInfo(request, userDTO.getUserDTO().getId()));
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<ResultResponse> delUser(@AuthenticationPrincipal CustomUserDetails userDTO) {
        if (userDTO == null || userDTO.getUserDTO().getId() < 1) {
            throw new DataNotFoundException(ErrorCode.DEFAULT_NOT_FOUND);
        }
        return ResponseEntity.ok(userService.deleteUser(userDTO.getUserDTO().getId()));
    }


    @DeleteMapping("/users/me1")
    public ResponseEntity<ResultResponse> delUser1(@AuthenticationPrincipal CustomUserDetails userDTO) {
        return ResponseEntity.ok(userService.getUserInfo(userDTO.getUserDTO().getId()));
    }


}
