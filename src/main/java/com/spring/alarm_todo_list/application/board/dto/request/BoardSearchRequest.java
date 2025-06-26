package com.spring.alarm_todo_list.application.board.dto.request;

import com.spring.alarm_todo_list.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardSearchRequest {
    private String boardTitle;
    private LocalDate boardDate;
    private String boardContent;
    private long page;
    private long size;

    public static BoardSearchRequest from(Board board, long page, long size) {
        return new BoardSearchRequest(board.getTitle(), board.getBoardDate(), board.getContent(), page, size);
    }
}
