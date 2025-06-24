package com.spring.alarm_todo_list.application.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.alarm_todo_list.application.account.dto.request.AccountInfo;
import com.spring.alarm_todo_list.application.board.dto.request.BoardSearchRequest;
import com.spring.alarm_todo_list.application.board.dto.request.BoardUpdateRequest;
import com.spring.alarm_todo_list.application.board.dto.response.BoardSearchListResponse;
import com.spring.alarm_todo_list.application.board.service.BoardReadService;
import com.spring.alarm_todo_list.application.board.service.BoardWriteService;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import com.spring.alarm_todo_list.util.PaginationResponse;
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
import static org.hamcrest.Matchers.containsString;
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
    @DisplayName("제목에 '둥이'를 포함한 Todo 검색할 시 성공적으로 처리된다.")
    public void successTitleTodoSearch() throws Exception {
        //given
        String title = "둥이";
        String content = "test내용";
        LocalDate boardDate = LocalDate.of(2025, 05, 30);
        LocalTime boardTime = LocalTime.of(23,11);

        BoardSearchListResponse response = new BoardSearchListResponse(1L, title, content, boardDate, boardTime, BoardType.TODO);

        PaginationResponse<BoardSearchListResponse> mockResponse = new PaginationResponse<BoardSearchListResponse>(
            List.of(response), 1, 10, 1L);

        when(boardReadService.findAll(any(AccountInfo.class), any(BoardSearchRequest.class)))
                .thenReturn(mockResponse);

        //when + then
        mockMvc.perform(get("/board")
                        .param("boardTitle", title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].title")
                        .value(containsString("둥이")));
    }

    @Test
    @DisplayName("내용에 '둥이'를 포함한 Todo 검색할 시 성공적으로 처리된다.")
    public void successContentTodoSearch() throws Exception {
        //given
        String title = "test제목";
        String content = "저희집 강아지는 둥이 입니다.";
        LocalDate boardDate = LocalDate.of(2025, 05, 30);
        LocalTime boardTime = LocalTime.of(23,11);

        BoardSearchListResponse response = new BoardSearchListResponse(1L, title, content, boardDate, boardTime, BoardType.TODO);

        PaginationResponse<BoardSearchListResponse> mockResponse = new PaginationResponse<BoardSearchListResponse>(
                List.of(response), 1, 10, 1L);

        when(boardReadService.findAll(any(AccountInfo.class), any(BoardSearchRequest.class)))
                .thenReturn(mockResponse);

        //when + then
        mockMvc.perform(get("/board")
                        .param("boardContent", content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].content")
                        .value(containsString("둥이")));
    }

    @Test
    @DisplayName("사용자가 검색한 날짜를 포함한 Todo 검색할 시 성공적으로 처리된다.")
    public void successBoardDateTodoSearch() throws Exception {
        //given
        String title = "test제목";
        String content = "test내용";
        LocalDate boardDate = LocalDate.of(2025, 05, 30);
        LocalTime boardTime = LocalTime.of(23,11);

        BoardSearchListResponse response = new BoardSearchListResponse(1L, title, content, boardDate, boardTime, BoardType.TODO);

        PaginationResponse<BoardSearchListResponse> mockResponse = new PaginationResponse<BoardSearchListResponse>(
                List.of(response), 1, 10, 1L);

        when(boardReadService.findAll(any(AccountInfo.class), any(BoardSearchRequest.class)))
                .thenReturn(mockResponse);

        //when + then
        mockMvc.perform(get("/board")
                        .param("boardDate", boardDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].boardDate")
                        .value(containsString(String.valueOf(LocalDate.of(2025,05,30)))));
    }

    @Test
    @DisplayName("일정 제목, 내용, 날짜를 모두 포함한 Todo 검색할 시 성공적으로 처리된다.")
    public void successFindAllTodoSearch() throws Exception {
        //given
        String title = "test제목";
        String content = "test내용";
        LocalDate boardDate = LocalDate.of(2025, 05, 30);
        LocalTime boardTime = LocalTime.of(23,11);

        BoardSearchListResponse response = new BoardSearchListResponse(1L, title, content, boardDate, boardTime, BoardType.TODO);

        PaginationResponse<BoardSearchListResponse> mockResponse = new PaginationResponse<BoardSearchListResponse>(
                List.of(response), 1, 10, 1L);

        when(boardReadService.findAll(any(AccountInfo.class), any(BoardSearchRequest.class)))
                .thenReturn(mockResponse);

        //when + then
        mockMvc.perform(get("/board")
                        .param("title", title)
                        .param("content", content)
                        .param("boardDate", boardDate.toString())
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].title")
                        .value(containsString("test제목")))
                .andExpect(jsonPath("$.contents[0].content")
                        .value(containsString("test내용")))
                .andExpect(jsonPath("$.contents[0].boardDate")
                        .value(containsString(String.valueOf(LocalDate.of(2025,05,30)))));
    }
}