package com.spring.alarm_todo_list.application.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.application.board.service.BoardReadService;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import com.spring.alarm_todo_list.application.jwt.JwtTokenProvider;
import com.spring.alarm_todo_list.config.JwtAccessDeniedHandler;
import com.spring.alarm_todo_list.config.JwtAuthenticationEntryPoint;
import com.spring.alarm_todo_list.config.SecurityConfig;
import com.spring.alarm_todo_list.exception.ErrorHandler;
import com.spring.alarm_todo_list.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class, ErrorHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BoardWriteService boardWriteService;

    @MockitoBean
    BoardReadService boardReadService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockitoBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("사용자가 등록한 일정을 수정할 시 성공적으로 처리된다.")
    public void successTodoUpdate() throws Exception {
        //given
        Long accountId = 1L;
        Long boardId = 1L;
        BoardUpdateRequest request = new BoardUpdateRequest("00공부", "ch01-02", LocalDate.of(2025,01,01), LocalTime.of(16,00));

        //when + then
        when(boardWriteService.update(eq(accountId), eq(boardId), any(BoardUpdateRequest.class)))
                .thenReturn("일정 수정 성공");

        mockMvc.perform(put("/board/{accountId}/{boardId}", accountId, boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                .andExpect(content().string("일정 수정 성공"));
    }
}