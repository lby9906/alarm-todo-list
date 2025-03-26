package com.spring.alarm_todo_list.application.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardResponse {
    private String nickName;

    private List<BoardListResponse> boardListResponseList;
}
