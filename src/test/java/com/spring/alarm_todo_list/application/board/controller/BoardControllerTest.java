package com.spring.alarm_todo_list.application.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchListResponse;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchResponse;
import com.spring.alarm_todo_list.application.board.service.BoardReadService;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @InjectMocks
    BoardController boardController;

    @Mock
    BoardWriteService boardWriteService;

    @Mock
    BoardReadService boardReadService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .build();
    }

    @Test
    @DisplayName("사용자가 등록한 일정을 수정할 시 성공적으로 처리된다.")
    public void successTodoUpdate() throws Exception {
        //given
        Long boardId = 1L;
        BoardUpdateRequest request = new BoardUpdateRequest("00공부", "ch01-02", LocalDate.of(2025,01,01), LocalTime.of(16,00));

        //when + then
        when(boardWriteService.update(any(AccountInfo.class), eq(boardId), any(BoardUpdateRequest.class)))
                .thenReturn("일정 수정 성공");

        mockMvc.perform(put("/board/{boardId}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자가 등록한 일정을 검색할 시 성공적으로 처리된다.")
    public void successTodoSearch() throws Exception {
        //given
        String title = "test제목";
        String content = "test내용";
        LocalDate boardDate = LocalDate.of(2025, 05, 30);
        LocalTime boardTime = LocalTime.of(23,11);

        BoardSearchResponse mockResponse = new BoardSearchResponse(
                List.of(new BoardSearchListResponse(1L, title, content ,boardDate, boardTime, BoardType.TODO)));

        when(boardReadService.findSearch(eq(title), eq(boardDate), eq(content), any(AccountInfo.class)))
                .thenReturn(mockResponse);

        //when + then
        mockMvc.perform(get("/board/search")
                .param("boardTitle", title)
                .param("boardDate", boardDate.toString())
                .param("boardContent", content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardSearchListResponses[0].title").value(title))
                .andExpect(jsonPath("$.boardSearchListResponses[0].content").value(content))
                .andExpect(jsonPath("$.boardSearchListResponses[0].boardDate[0]").value(2025))
                .andExpect(jsonPath("$.boardSearchListResponses[0].boardDate[1]").value(5))
                .andExpect(jsonPath("$.boardSearchListResponses[0].boardDate[2]").value(30));
    }
}