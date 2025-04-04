package com.spring.alarm_todo_list.application.board.dto.response;

import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class BoardListResponse {
    private Long boardId;

    private String title;

    private LocalDate boardDate;

    private LocalTime boardTime;

    private BoardType boardType;
}
