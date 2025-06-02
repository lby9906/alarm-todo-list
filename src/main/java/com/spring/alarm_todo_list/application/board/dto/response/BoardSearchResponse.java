package com.spring.alarm_todo_list.application.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardSearchResponse {

    private List<BoardSearchListResponse> boardSearchListResponses;
}