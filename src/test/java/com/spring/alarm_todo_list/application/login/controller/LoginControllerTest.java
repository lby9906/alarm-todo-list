package com.spring.alarm_todo_list.application.login.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.alarm_todo_list.QuerydslConfig;
import com.spring.alarm_todo_list.application.jwt.JwtTokenProvider;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginRequestDto;
import com.spring.alarm_todo_list.application.login.dto.AccountLoginResponseDto;
import com.spring.alarm_todo_list.application.login.service.LoginService;
import com.spring.alarm_todo_list.config.JwtAccessDeniedHandler;
import com.spring.alarm_todo_list.config.JwtAuthenticationEntryPoint;
import com.spring.alarm_todo_list.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import({SecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("사용자가 정상적인 로그인 시 accessToken과 사용자 정보가 성공적으로 반환된다.")
    public void successLoginResponse() throws Exception {
        //given
        AccountLoginRequestDto requestDto = new AccountLoginRequestDto("test@test.com", "0000");
        AccountLoginResponseDto responseDto = new AccountLoginResponseDto(1L, "test", "fake-jwt-token", LocalDateTime.now().plusHours(1));

        given(loginService.login(any(AccountLoginRequestDto.class))).willReturn(responseDto);

        //when&then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickName").value("test"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.expiresAt").exists());

    }
}