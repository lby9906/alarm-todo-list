package com.spring.alarm_todo_list.application.login.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.alarm_todo_list.application.login.dto.request.AccountLoginRequestDto;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginResponseDto;
import com.spring.alarm_todo_list.application.login.service.LoginService;
import com.spring.alarm_todo_list.exception.AlarmTodoListException;
import com.spring.alarm_todo_list.exception.ErrorCode;
import com.spring.alarm_todo_list.exception.ErrorHandler;
import com.spring.alarm_todo_list.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    MockMvc mockMvc;

    @Mock
    LoginService loginService;

    ObjectMapper objectMapper;

    @InjectMocks
    LoginController loginController;

    @Mock
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionHandler(errorHandler))
                .build();
    }

    @Test
    @DisplayName("사용자가 정상적인 로그인 시 accessToken과 사용자 정보가 성공적으로 반환된다.")
    public void successLoginResponse() throws Exception {
        //given
        AccountLoginRequestDto requestDto = new AccountLoginRequestDto("test@test.com", "0000");
        AccountLoginResponseDto responseDto = new AccountLoginResponseDto(1L, "test", "fake-jwt-access-token",
                LocalDateTime.now().plusHours(1), "fake-jwt-refresh-token", LocalDateTime.now().plusHours(1));

        given(loginService.login(any(AccountLoginRequestDto.class))).willReturn(responseDto);

        //when&then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickName").value("test"))
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-access-token"))
                .andExpect(jsonPath("$.accessExpiresAt").exists())
                .andExpect(jsonPath("$.refreshToken").value("fake-jwt-refresh-token"))
                .andExpect(jsonPath("$.refreshExpiresAt").exists());

    }

    @Test
    @DisplayName("사용자가 이메일 또는 비밀번호를 잘못 입력했을 때 예외가 발생한다.")
    public void occurEmailPasswordException() throws Exception{
        //given
        AccountLoginRequestDto requestDto = new AccountLoginRequestDto("test@test.com", "0000");

        //when
        when(errorHandler.handleExceptionInternal(any(ErrorCode.class)))
                .thenAnswer(invocation -> {
                    ErrorCode errorCode = invocation.getArgument(0);
                    Map<String, Object> body = new HashMap<>();
                    body.put("message", errorCode.getMessage());  // 에러 메시지
                    return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
                });

        when(loginService.login(any(AccountLoginRequestDto.class)))
                .thenThrow(new AlarmTodoListException(ErrorCode.NOT_MATCH_EMAIL_PASSWORD));

        //then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일 또는 비밀번호가 일치하지 않습니다."))
                .andDo(print());
    }
}