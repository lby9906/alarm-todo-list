package com.spring.alarm_todo_list.application.board.dto.response;

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
public class BoardListResponse {
    private Long boardId;

    private String title;

    private LocalDate boardDate;

    private LocalTime boardTime;

    private BoardType boardType;

    public static BoardListResponse from(Board board) {
        return new BoardListResponse(board.getId(), board.getTitle(), board.getBoardDate(),
                board.getBoardTime(), board.getBoardType());
    }

    public static List<BoardListResponse> from (List<Board> boards) {
        return boards.stream()
                .map(board -> BoardListResponse.from(board))
                .collect(Collectors.toList());
    }
}
