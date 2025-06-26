package com.spring.alarm_todo_list.application.board.dto.response;

import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

class BoardSearchResponseTest {

    @Test
    @DisplayName("response의 일정id, 제목, 일정 날짜, 일정 시간, 일정 타입인 boardList가 올바르게 반환된다.")
    public void successBoardSearchResponse() {
        //given
        String nickName = "test";
        String title = "test제목";
        String content = "test내용";
        LocalDate boardDate = LocalDate.of(2025, 03, 11);
        LocalTime boardTime = LocalTime.of(23, 11);
        BoardType boardType = BoardType.TODO;

        List<BoardSearchListResponse> boardList = List.of(
                new BoardSearchListResponse(1L, title, content ,boardDate, boardTime, boardType)
        );

        //when
        BoardSearchResponse boardSearchResponse = new BoardSearchResponse(boardList);

        //then
        List<BoardSearchListResponse> responses = boardSearchResponse.getBoardSearchListResponses();
        assertThat(responses).extracting("boardId", "title", "boardDate", "boardTime", "boardType")
                .contains(tuple(1L, "test제목",
                        LocalDate.of(2025, 03, 11),
                        LocalTime.of(23, 11),
                        BoardType.TODO));
    }

    @Test
    @DisplayName("response의 제목에 '둥이'로 검색할 시 boardList가 올바르게 반환된다.")
    public void successTitleSearchResponse() {
        //given
        String nickName = "test";
        String title = "둥이";
        String content = "test내용";
        LocalDate boardDate = LocalDate.of(2025, 03, 11);
        LocalTime boardTime = LocalTime.of(23, 11);
        BoardType boardType = BoardType.TODO;

        List<BoardSearchListResponse> boardList = List.of(
                new BoardSearchListResponse(1L, title, content ,boardDate, boardTime, boardType)
        );

        //when
        BoardSearchResponse boardSearchResponse = new BoardSearchResponse(boardList);

        //then
        List<BoardSearchListResponse> responses = boardSearchResponse.getBoardSearchListResponses();
        assertThat(responses).extracting("boardId", "title", "boardDate", "boardTime", "boardType")
                .contains(tuple(1L, "둥이",
                        LocalDate.of(2025, 03, 11),
                        LocalTime.of(23, 11),
                        BoardType.TODO));
    }

    @Test
    @DisplayName("response의 내용에 '둥이'로 검색할 시 boardList가 올바르게 반환된다.")
    public void successContentSearchResponse() {
        //given
        String nickName = "test";
        String title = "test제목";
        String content = "우리집 둥이는 6살";
        LocalDate boardDate = LocalDate.of(2025, 03, 11);
        LocalTime boardTime = LocalTime.of(23, 11);
        BoardType boardType = BoardType.TODO;

        List<BoardSearchListResponse> boardList = List.of(
                new BoardSearchListResponse(1L, title, content ,boardDate, boardTime, boardType)
        );

        //when
        BoardSearchResponse boardSearchResponse = new BoardSearchResponse(boardList);

        //then
        List<BoardSearchListResponse> responses = boardSearchResponse.getBoardSearchListResponses();
        assertThat(responses).extracting("boardId", "title", "content" ,"boardDate", "boardTime", "boardType")
                .contains(tuple(1L, "test제목", "우리집 둥이는 6살",
                        LocalDate.of(2025, 03, 11),
                        LocalTime.of(23, 11),
                        BoardType.TODO));
    }
}