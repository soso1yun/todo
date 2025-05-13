package com.llm.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llm.todo.domain.user.dto.LoginRequest;
import com.llm.todo.domain.user.dto.SignUpRequest;
import com.llm.todo.domain.user.dto.UserRequest;
import com.llm.todo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUp() throws Exception {
        userRepository.deleteAll();

        // 회원가입 요청
        SignUpRequest signUpRequest = new SignUpRequest("test", "usr1", "admin1234", "ROLE_USER");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 로그인 요청 (토큰 생성)
        LoginRequest loginRequest = new LoginRequest("usr1", "admin1234");
        MvcResult result = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // JWT 토큰 확보
        jwtToken = objectMapper.readTree(result.getResponse().getContentAsString()).get("message").asText();
    }


    @Test
    @Order(1)
    @DisplayName("1. 로그인 비밀번호 다를경우")
    void loginPasswordNotMatches() throws Exception {
        LoginRequest loginRequest = new LoginRequest("usr1", "admin14");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("로그인 정보가 일치하지 않습니다."))
                .andReturn();
    }


    @Test
    @Order(2)
    @DisplayName("2. 회원 정보 가져오기 성공")
    void testGetUserInfo() throws Exception {
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userInfo.userId").value("usr1"));
    }

    @Test
    @Order(3)
    @DisplayName("3. 잘못된 토큰으로 회원 정보 가져오기 실패")
    void testGetUserInfoWithInvalidToken() throws Exception {
        String invalidToken = "invalid.jwt.token";
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized()) // 잘못된 토큰에 대한 isUnauthorized 처리
        ;
    }

    @Test
    @Order(4)
    @DisplayName("4. 회원 비밀번호 변경 성공 - 유효한 비밀번호")
    void testUpdateUserPasswordSuccess() throws Exception {
        UserRequest updateRequest = new UserRequest("", "newPass123");
        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("수정이 완료되었습니다. 다시 로그인해주세요."));
    }

    @Test
    @Order(5)
    @DisplayName("5. 회원정보 수정 실패 - 잘못된 비밀번호 형식")
    void testUpdateUserWithInvalidPassword() throws Exception {
        UserRequest updateRequest = new UserRequest("", "short");
        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound()) // 비밀번호 형식 오류로 NotFound 처리
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("데이터 형식을 확인해주세요.")); // ErrorCode.VALIDATION_ERROR.getMessage()
    }


    @Test
    @Order(6)
    @DisplayName("6. 중복된 회원가입 실패")
    void testSignUpWithDuplicateUserId() throws Exception {
        SignUpRequest duplicateRequest = new SignUpRequest("test", "usr1", "admin1234", "ROLE_USER");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isBadRequest()) // DuplicateException 처리
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("중복입니다.")); // ErrorCode.DEFAULT_DUPLICATE.getMessage()
    }

    @Test
    @Order(7)
    @DisplayName("7. 로그인 실패 - 잘못된 비밀번호")
    void testLoginWithInvalidPassword() throws Exception {
        LoginRequest invalidPasswordRequest = new LoginRequest("testUser", "wrongPassword");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPasswordRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("데이터가 없습니다.")); // ResultMessage.INVALID_LOGIN_DATA.getMessage()
    }


    @Test
    @Order(8)
    @DisplayName("8. 회원 탈퇴 성공")
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("삭제가 완료되었습니다."));
    }

    @Test
    @Order(9)
    @DisplayName("9. 로그인 실패 - 존재하지 않는 사용자")
    void testLoginWithNonExistentUser() throws Exception {
        LoginRequest nonexistentUserRequest = new LoginRequest("ssss", "admin1234");
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonexistentUserRequest)))
                .andExpect(status().isNotFound()) // DataNotFoundException 처리
                .andExpect(jsonPath("$.code").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("데이터가 없습니다.")); // ErrorCode.DEFAULT_NOT_FOUND.getMessage()
    }
}