package com.spring.alarm_todo_list.domain.board.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    TODO("할 일"), DONE("완료");

    private final String content;
}
