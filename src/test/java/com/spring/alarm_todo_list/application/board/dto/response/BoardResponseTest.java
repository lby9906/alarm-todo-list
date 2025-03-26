package com.spring.alarm_todo_list.application.board.dto.response;

import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class BoardResponseTest {

    @Test
    @Transactional
    @DisplayName("response의 닉네임과 bordList가 올바르게 반환된다.")
    public void successBoardResponse() {
        //given
        String nickName = "test";
        String title = "test제목";
        String content = "test내용";
        LocalDate boardDate = LocalDate.of(2025, 03, 11);
        LocalTime boardTime = LocalTime.of(23, 11);
        BoardType boardType = BoardType.TODO;

        List<BoardListResponse> boardList = List.of(
                new BoardListResponse(1L, title, boardDate, boardTime, boardType)
        );

        //when
        BoardResponse boardResponse = new BoardResponse(nickName, boardList);

        //then
        assertThat(boardResponse.getNickName()).isEqualTo(nickName);
        assertThat(boardResponse.getBoardListResponseList()).hasSize(1);

        assertThat(boardList).extracting(
                "boardId", "title", "boardDate", "boardTime", "boardType")
                .contains(tuple(1L, "test제목",
                        LocalDate.of(2025,03,11),
                        LocalTime.of(23,11),
                        BoardType.TODO));
    }
}