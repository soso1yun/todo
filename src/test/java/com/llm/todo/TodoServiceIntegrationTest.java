package com.llm.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llm.todo.domain.todo.dto.TodoRequest;
import com.llm.todo.domain.todo.dto.TodoSearchRequest;
import com.llm.todo.domain.todo.repository.TodoRepository;
import com.llm.todo.domain.user.dto.LoginRequest;
import com.llm.todo.domain.user.dto.SignUpRequest;
import com.llm.todo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest // Spring Boot 테스트 설정
@AutoConfigureMockMvc // MockMVC 자동 설정
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;


    private String jwtToken; // 전역 JWT 토큰

    private String jwtToken2;


    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    void setUp() throws Exception {

        todoRepository.deleteAll();
        userRepository.deleteAll();

        // 회원가입 요청
        SignUpRequest signUpRequest = new SignUpRequest("test", "usr2", "admin1234", "ROLE_USER");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 로그인 요청 (토큰 생성)
        LoginRequest loginRequest = new LoginRequest("usr2", "admin1234");
        MvcResult result = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // JWT 토큰 확보
        jwtToken = objectMapper.readTree(result.getResponse().getContentAsString()).get("message").asText();


        //========= 2번째 회원
        SignUpRequest signUpRequest2 = new SignUpRequest("test", "usr2", "admin1234", "ROLE_USER");
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest2)));

        // 로그인 요청 (토큰 생성)
        LoginRequest loginRequest2 = new LoginRequest("usr2", "admin1234");
        MvcResult result2 = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest2)))
                .andReturn();

        // JWT 토큰 확보
        jwtToken2 = objectMapper.readTree(result2.getResponse().getContentAsString()).get("message").asText();
    }

    @Test
    @Order(1)
    @DisplayName("1. todo 등록 성공")
    void testAddTodos() throws Exception {
        TodoRequest todoRequest = new TodoRequest("New Title", "New Description", "2025-06-01", "대기");

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("등록이 완료되었습니다."))); // ResultMessage.ADD_COMPLETED.getMessage()
    }

    @Test
    @Order(2)
    @DisplayName("2. 전체 todo 조회")
    void testGetTodos() throws Exception {
        mockMvc.perform(get("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.todoList", hasSize(1)));
    }

    @Test
    @Order(3)
    @DisplayName("3. ID 기반 단건 todo 조회 성공")
    void testGetTodoById() throws Exception {
        mockMvc.perform(get("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.todoData.title", is("New Title")));
    }

    @Test
    @Order(4)
    @DisplayName("4. ID 기반 단건 todo 조회 성공( 다른 사용자 )")
    void testGetTodoByIdForbidden() throws Exception {
        mockMvc.perform(get("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.todoData.title", is("New Title")));
    }

    @Test
    @Order(5)
    @DisplayName("5. todo 업데이트 성공")
    void testUpdateTodo() throws Exception {
        TodoRequest updateRequest = new TodoRequest("Updated Title", "Updated Description", "2025-06-15", "완료");

        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("수정이 완료되었습니다."))); // ResultMessage.EDIT_COMPLETED.getMessage()
    }

    @Test
    @Order(6)
    @DisplayName("6. todo 검색 성공")
    void testSearchTodos() throws Exception {
        TodoSearchRequest searchRequest = new TodoSearchRequest("title", "2025-06-15", "2025-06-15", "");

        mockMvc.perform(get("/todos/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.todoList", hasSize(1)));
    }


    @Test
    @Order(7)
    @DisplayName("7. 검색 결과 없음")
    void testSearchTodosEmpty() throws Exception {
        TodoSearchRequest searchRequest = new TodoSearchRequest("nonexistent", "2025-05-12", "2025-05-15", "");

        mockMvc.perform(get("/todos/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.todoList", hasSize(0)));
    }

    @Test
    @Order(8)
    @DisplayName("8. 존재하지 않는 todo ID 조회")
    void testGetTodoByIdNotFound() throws Exception {
        mockMvc.perform(get("/todos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("DATA_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("데이터가 없습니다."))); // ErrorCode.DEFAULT_NOT_FOUND
    }


    @Test
    @Order(9)
    @DisplayName("9. todo 삭제 성공")
    void testDeleteTodo() throws Exception {
        mockMvc.perform(delete("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("삭제가 완료되었습니다."))); // ResultMessage.DELETE_COMPLETED.getMessage()
    }


}
