package com.spring.alarm_todo_list.application.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.alarm_todo_list.domain.board.entity.Board;
import com.spring.alarm_todo_list.domain.board.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BoardSearchListResponse {

    private Long boardId;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate boardDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime boardTime;

    private BoardType boardType;

    public static BoardSearchListResponse from(Board board) {
        return new BoardSearchListResponse(board.getId(), board.getTitle(), board.getContent(),
                board.getBoardDate(), board.getBoardTime(), board.getBoardType());
    }

    public static List<BoardSearchListResponse> from(List<Board> boards) {
        return boards.stream()
                .map(board -> BoardSearchListResponse.from(board))
                .collect(Collectors.toList());
    }

    public static BoardSearchListResponse of(Board board) {
        return new BoardSearchListResponse(board.getId(), board.getTitle(), board.getContent(),
                board.getBoardDate(), board.getBoardTime(), board.getBoardType());
    }
}